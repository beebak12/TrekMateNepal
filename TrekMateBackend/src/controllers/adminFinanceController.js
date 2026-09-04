const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const COMMISSION_RATE = 10;

const invalid = (req, res) => {
  const errors = validationResult(req);
  if (errors.isEmpty()) return false;
  res.status(400).json({ success: false, message: 'Validation failed', errors: errors.array() });
  return true;
};

const fail = (res, error, message) => {
  console.error(error);
  return res.status(500).json({ success: false, message });
};

const log = (db, adminId, action, type, id, details = {}) => db.query(
  `INSERT INTO admin_logs (user_id, action, entity_type, entity_id, details) VALUES (?, ?, ?, ?, ?)`,
  [adminId, action, type, id || null, JSON.stringify(details)]
);

const getDashboard = async (req, res) => {
  try {
    const pool = getPool();
    const [[payments]] = await pool.query(
      `SELECT
        COALESCE(SUM(CASE WHEN payment_status IN ('VERIFIED','PARTIALLY_REFUNDED') THEN gross_amount ELSE 0 END),0) total_booking_amount,
        COALESCE(SUM(CASE WHEN payment_status IN ('VERIFIED','PARTIALLY_REFUNDED') THEN commission_amount ELSE 0 END),0) trekmate_revenue,
        COALESCE(SUM(verification_status = 'VERIFIED'),0) verified_transactions,
        COALESCE(SUM(verification_status = 'UNVERIFIED'),0) unverified_transactions
       FROM payment_transactions`
    );
    const [[payouts]] = await pool.query(
      `SELECT COALESCE(SUM(CASE WHEN status = 'PENDING' THEN amount ELSE 0 END),0) pending_payables,
              COALESCE(SUM(CASE WHEN status = 'PAID' THEN amount ELSE 0 END),0) completed_payouts,
              COALESCE(SUM(status = 'PENDING'),0) pending_payout_count FROM provider_payouts`
    );
    const [[refunds]] = await pool.query(
      `SELECT COALESCE(SUM(CASE WHEN status = 'COMPLETED' THEN amount ELSE 0 END),0) total_refunds,
              COALESCE(SUM(status = 'REQUESTED'),0) pending_refund_count FROM payment_refunds`
    );
    return res.json({ success: true, data: { ...payments, ...payouts, ...refunds, commission_rate: COMMISSION_RATE } });
  } catch (error) {
    return fail(res, error, 'Failed to fetch dashboard');
  }
};

const getTransactions = async (req, res) => {
  try {
    const status = String(req.query.status || '').toUpperCase();
    const search = String(req.query.search || '').trim();
    const where = [];
    const values = [];
    if (status) { where.push('pt.payment_status = ?'); values.push(status); }
    if (search) {
      where.push('(pt.transaction_reference LIKE ? OR c.full_name LIKE ? OR p.full_name LIKE ?)');
      values.push(`%${search}%`, `%${search}%`, `%${search}%`);
    }
    const [rows] = await getPool().query(
      `SELECT pt.*, c.full_name customer_name, c.email customer_email,
              p.full_name provider_name, p.email provider_email, pp.status payout_status
       FROM payment_transactions pt JOIN users c ON c.id = pt.customer_id
       LEFT JOIN users p ON p.id = pt.provider_id
       LEFT JOIN provider_payouts pp ON pp.transaction_id = pt.id
       ${where.length ? `WHERE ${where.join(' AND ')}` : ''}
       ORDER BY pt.created_at DESC`, values
    );
    return res.json({ success: true, data: rows });
  } catch (error) {
    return fail(res, error, 'Failed to fetch transactions');
  }
};

const createTransaction = async (req, res) => {
  if (invalid(req, res)) return;
  const gross = Number(req.body.gross_amount);
  const commission = Number((gross * COMMISSION_RATE / 100).toFixed(2));
  const payable = Number((gross - commission).toFixed(2));
  try {
    const pool = getPool();
    const reference = req.body.transaction_reference || `TXN-${Date.now()}`;
    const [result] = await pool.query(
      `INSERT INTO payment_transactions
       (transaction_reference,gateway_transaction_id,rental_id,package_booking_id,customer_id,provider_id,
        gateway,gross_amount,commission_rate,commission_amount,provider_payable,gateway_response)
       VALUES (?,?,?,?,?,?,?,?,?,?,?,?)`,
      [reference, req.body.gateway_transaction_id || null, req.body.rental_id || null,
        req.body.package_booking_id || null, req.body.customer_id, req.body.provider_id || null,
        req.body.gateway || 'SANDBOX', gross, COMMISSION_RATE, commission, payable,
        JSON.stringify(req.body.gateway_response || {})]
    );
    await log(pool, req.user.id, 'CREATE_TRANSACTION', 'payment_transaction', result.insertId, { reference });
    return res.status(201).json({
      success: true,
      message: 'Transaction recorded; gateway verification is still required',
      data: { id: result.insertId, transaction_reference: reference, gross_amount: gross,
        commission_amount: commission, provider_payable: payable,
        payment_status: 'PENDING', verification_status: 'UNVERIFIED' },
    });
  } catch (error) {
    if (error.code === 'ER_DUP_ENTRY') return res.status(409).json({ success: false, message: 'Transaction ID already exists' });
    return fail(res, error, 'Failed to create transaction');
  }
};

const verifyTransaction = async (req, res) => {
  if (invalid(req, res)) return;
  const db = await getPool().getConnection();
  try {
    await db.beginTransaction();
    const [rows] = await db.query('SELECT * FROM payment_transactions WHERE id = ? FOR UPDATE', [req.params.id]);
    if (!rows.length) { await db.rollback(); return res.status(404).json({ success: false, message: 'Transaction not found' }); }
    const transaction = rows[0];
    if (transaction.verification_status === 'VERIFIED') {
      await db.rollback();
      return res.status(409).json({ success: false, message: 'Transaction already verified' });
    }
    const verified = req.body.verified === true;
    const gatewayId = req.body.gateway_transaction_id || transaction.gateway_transaction_id;
    if (verified && !gatewayId) {
      await db.rollback();
      return res.status(400).json({ success: false, message: 'Gateway transaction ID is required' });
    }
    await db.query(
      `UPDATE payment_transactions SET gateway_transaction_id = ?, payment_status = ?,
       verification_status = ?, verified_at = ?, gateway_response = ? WHERE id = ?`,
      [gatewayId || null, verified ? 'VERIFIED' : 'FAILED', verified ? 'VERIFIED' : 'FAILED',
        verified ? new Date() : null, JSON.stringify(req.body.gateway_response || {}), transaction.id]
    );
    if (verified && transaction.provider_id) {
      await db.query(
        `INSERT INTO provider_payouts (transaction_id,provider_id,amount) VALUES (?,?,?)
         ON DUPLICATE KEY UPDATE provider_id=VALUES(provider_id), amount=VALUES(amount)`,
        [transaction.id, transaction.provider_id, transaction.provider_payable]
      );
    }
    await log(db, req.user.id, verified ? 'VERIFY_TRANSACTION' : 'FAIL_TRANSACTION',
      'payment_transaction', transaction.id, { gateway_transaction_id: gatewayId || null });
    await db.commit();
    return res.json({ success: true, message: verified ? 'Transaction verified' : 'Transaction marked failed' });
  } catch (error) {
    await db.rollback();
    return fail(res, error, 'Failed to verify transaction');
  } finally {
    db.release();
  }
};

const getPayouts = async (req, res) => {
  try {
    const status = String(req.query.status || '').toUpperCase();
    const [rows] = await getPool().query(
      `SELECT pp.*, pt.transaction_reference, u.full_name provider_name, u.email provider_email
       FROM provider_payouts pp JOIN payment_transactions pt ON pt.id=pp.transaction_id
       JOIN users u ON u.id=pp.provider_id ${status ? 'WHERE pp.status=?' : ''}
       ORDER BY pp.created_at DESC`, status ? [status] : []
    );
    return res.json({ success: true, data: rows });
  } catch (error) {
    return fail(res, error, 'Failed to fetch payouts');
  }
};

const updatePayout = async (req, res) => {
  if (invalid(req, res)) return;
  const next = req.body.status.toUpperCase();
  const db = await getPool().getConnection();
  try {
    await db.beginTransaction();
    const [rows] = await db.query('SELECT * FROM provider_payouts WHERE id=? FOR UPDATE', [req.params.id]);
    if (!rows.length) { await db.rollback(); return res.status(404).json({ success: false, message: 'Payout not found' }); }
    const transitions = { PENDING: ['APPROVED','REJECTED'], APPROVED: ['PAID','REJECTED'], PAID: [], REJECTED: [] };
    if (!transitions[rows[0].status].includes(next)) {
      await db.rollback();
      return res.status(409).json({ success: false, message: `Cannot change ${rows[0].status} payout to ${next}` });
    }
    if (next === 'PAID' && !req.body.payout_reference) {
      await db.rollback();
      return res.status(400).json({ success: false, message: 'Payout reference is required' });
    }
    await db.query(
      `UPDATE provider_payouts SET status=?, approved_by=?,
       approved_at=IF(?='APPROVED',NOW(),approved_at), paid_at=IF(?='PAID',NOW(),paid_at),
       payout_reference=COALESCE(?,payout_reference), notes=COALESCE(?,notes) WHERE id=?`,
      [next, req.user.id, next, next, req.body.payout_reference || null, req.body.notes || null, req.params.id]
    );
    await log(db, req.user.id, `PAYOUT_${next}`, 'provider_payout', req.params.id);
    await db.commit();
    return res.json({ success: true, message: `Payout marked ${next.toLowerCase()}` });
  } catch (error) {
    await db.rollback();
    return fail(res, error, 'Failed to update payout');
  } finally { db.release(); }
};

const getSettlements = async (req, res) => {
  try {
    const status = String(req.query.status || '').toUpperCase();
    const values = [];
    const where = status ? 'WHERE pb.status=?' : '';
    if (status) values.push(status);
    const [rows] = await getPool().query(
      `SELECT pb.id,pb.settlement_reference,pb.provider_id,
              DATE_FORMAT(pb.period_start,'%Y-%m-%d') period_start,
              DATE_FORMAT(pb.period_end,'%Y-%m-%d') period_end,
              pb.total_amount,pb.status,pb.approved_by,pb.approved_at,pb.paid_at,
              pb.payout_reference,pb.notes,pb.created_at,pb.updated_at,
              u.full_name provider_name,u.email provider_email,
              COUNT(pp.id) transaction_count
       FROM payout_batches pb JOIN users u ON u.id=pb.provider_id
       LEFT JOIN provider_payouts pp ON pp.batch_id=pb.id
       ${where} GROUP BY pb.id ORDER BY pb.period_end DESC,pb.created_at DESC`, values
    );
    return res.json({ success: true, data: rows });
  } catch (error) { return fail(res, error, 'Failed to fetch weekly settlements'); }
};

const createSettlement = async (req, res) => {
  if (invalid(req, res)) return;
  const { provider_id: providerId, period_start: periodStart, period_end: periodEnd } = req.body;
  const start = new Date(`${periodStart}T00:00:00Z`);
  const end = new Date(`${periodEnd}T00:00:00Z`);
  const days = Math.round((end - start) / 86400000) + 1;
  if (days < 1 || days > 7) {
    return res.status(400).json({ success: false, message: 'Settlement period must be between 1 and 7 days' });
  }
  const db = await getPool().getConnection();
  try {
    await db.beginTransaction();
    const [items] = await db.query(
      `SELECT id,amount FROM provider_payouts
       WHERE provider_id=? AND status='PENDING' AND batch_id IS NULL
         AND DATE(created_at) BETWEEN ? AND ? FOR UPDATE`,
      [providerId, periodStart, periodEnd]
    );
    if (!items.length) {
      await db.rollback();
      return res.status(409).json({ success: false, message: 'No pending payouts found for this provider and period' });
    }
    const total = items.reduce((sum, item) => sum + Number(item.amount), 0);
    const reference = `SET-${periodStart.replaceAll('-', '')}-${providerId}-${Date.now()}`;
    const [result] = await db.query(
      `INSERT INTO payout_batches
       (settlement_reference,provider_id,period_start,period_end,total_amount,notes)
       VALUES (?,?,?,?,?,?)`,
      [reference, providerId, periodStart, periodEnd, total.toFixed(2), req.body.notes || null]
    );
    await db.query(
      `UPDATE provider_payouts SET batch_id=? WHERE id IN (${items.map(() => '?').join(',')})`,
      [result.insertId, ...items.map((item) => item.id)]
    );
    await log(db, req.user.id, 'CREATE_WEEKLY_SETTLEMENT', 'payout_batch', result.insertId,
      { provider_id: providerId, period_start: periodStart, period_end: periodEnd, transaction_count: items.length });
    await db.commit();
    return res.status(201).json({ success: true, message: 'Weekly settlement created', data: {
      id: result.insertId, settlement_reference: reference, total_amount: total.toFixed(2),
      transaction_count: items.length, status: 'PENDING',
    } });
  } catch (error) {
    await db.rollback();
    return fail(res, error, 'Failed to create weekly settlement');
  } finally { db.release(); }
};

const updateSettlement = async (req, res) => {
  if (invalid(req, res)) return;
  const next = req.body.status.toUpperCase();
  const db = await getPool().getConnection();
  try {
    await db.beginTransaction();
    const [rows] = await db.query('SELECT * FROM payout_batches WHERE id=? FOR UPDATE', [req.params.id]);
    if (!rows.length) { await db.rollback(); return res.status(404).json({ success: false, message: 'Settlement not found' }); }
    const transitions = { PENDING: ['APPROVED','REJECTED'], APPROVED: ['PAID','REJECTED'], PAID: [], REJECTED: [] };
    if (!transitions[rows[0].status].includes(next)) {
      await db.rollback();
      return res.status(409).json({ success: false, message: `Cannot change ${rows[0].status} settlement to ${next}` });
    }
    if (next === 'PAID' && !req.body.payout_reference) {
      await db.rollback();
      return res.status(400).json({ success: false, message: 'Payout reference is required' });
    }
    await db.query(
      `UPDATE payout_batches SET status=?,approved_by=?,
       approved_at=IF(?='APPROVED',NOW(),approved_at),paid_at=IF(?='PAID',NOW(),paid_at),
       payout_reference=COALESCE(?,payout_reference),notes=COALESCE(?,notes) WHERE id=?`,
      [next, req.user.id, next, next, req.body.payout_reference || null, req.body.notes || null, req.params.id]
    );
    await db.query(
      `UPDATE provider_payouts SET status=?,approved_by=?,
       approved_at=IF(?='APPROVED',NOW(),approved_at),paid_at=IF(?='PAID',NOW(),paid_at),
       payout_reference=COALESCE(?,payout_reference) WHERE batch_id=?`,
      [next, req.user.id, next, next, req.body.payout_reference || null, req.params.id]
    );
    await log(db, req.user.id, `SETTLEMENT_${next}`, 'payout_batch', req.params.id);
    await db.commit();
    return res.json({ success: true, message: `Weekly settlement marked ${next.toLowerCase()}` });
  } catch (error) {
    await db.rollback();
    return fail(res, error, 'Failed to update weekly settlement');
  } finally { db.release(); }
};

const getMonthlyReport = async (req, res) => {
  try {
    const month = String(req.query.month || '').trim();
    if (!/^\d{4}-(0[1-9]|1[0-2])$/.test(month)) {
      return res.status(400).json({ success: false, message: 'Month must use YYYY-MM format' });
    }
    const start = `${month}-01`;
    const [year, monthNumber] = month.split('-').map(Number);
    const end = new Date(Date.UTC(year, monthNumber, 0)).toISOString().slice(0, 10);
    const pool = getPool();
    const [[summary]] = await pool.query(
      `SELECT COALESCE(SUM(gross_amount),0) gross_amount,
              COALESCE(SUM(commission_amount),0) trekmate_revenue,
              COALESCE(SUM(provider_payable),0) provider_payable,
              COALESCE(COUNT(*),0) transaction_count
       FROM payment_transactions
       WHERE verification_status='VERIFIED' AND DATE(verified_at) BETWEEN ? AND ?`, [start, end]
    );
    const [[refunds]] = await pool.query(
      `SELECT COALESCE(SUM(amount),0) refund_amount,COALESCE(COUNT(*),0) refund_count
       FROM payment_refunds WHERE status='COMPLETED' AND DATE(completed_at) BETWEEN ? AND ?`, [start, end]
    );
    const [[settlements]] = await pool.query(
      `SELECT COALESCE(SUM(total_amount),0) paid_to_providers,COALESCE(COUNT(*),0) settlement_count
       FROM payout_batches WHERE status='PAID' AND DATE(paid_at) BETWEEN ? AND ?`, [start, end]
    );
    const [providers] = await pool.query(
      `SELECT u.id provider_id,u.full_name provider_name,COUNT(pt.id) transaction_count,
              COALESCE(SUM(pt.gross_amount),0) gross_amount,
              COALESCE(SUM(pt.commission_amount),0) commission_amount,
              COALESCE(SUM(pt.provider_payable),0) provider_payable
       FROM payment_transactions pt JOIN users u ON u.id=pt.provider_id
       WHERE pt.verification_status='VERIFIED' AND DATE(pt.verified_at) BETWEEN ? AND ?
       GROUP BY u.id,u.full_name ORDER BY provider_payable DESC`, [start, end]
    );
    return res.json({ success: true, data: { month, period_start: start, period_end: end,
      summary: { ...summary, ...refunds, ...settlements }, providers } });
  } catch (error) { return fail(res, error, 'Failed to generate monthly report'); }
};

const getRefunds = async (req, res) => {
  try {
    const [rows] = await getPool().query(
      `SELECT pr.*,pt.transaction_reference,pt.gross_amount,u.full_name customer_name
       FROM payment_refunds pr JOIN payment_transactions pt ON pt.id=pr.transaction_id
       JOIN users u ON u.id=pt.customer_id ORDER BY pr.created_at DESC`
    );
    return res.json({ success: true, data: rows });
  } catch (error) { return fail(res, error, 'Failed to fetch refunds'); }
};

const createRefund = async (req, res) => {
  if (invalid(req, res)) return;
  try {
    const pool = getPool();
    const [[transaction]] = await pool.query(
      `SELECT pt.*,COALESCE((SELECT SUM(amount) FROM payment_refunds
       WHERE transaction_id=pt.id AND status IN ('APPROVED','COMPLETED')),0) refunded
       FROM payment_transactions pt WHERE pt.id=?`, [req.body.transaction_id]
    );
    if (!transaction) return res.status(404).json({ success: false, message: 'Transaction not found' });
    if (!['VERIFIED','PARTIALLY_REFUNDED'].includes(transaction.payment_status)) {
      return res.status(409).json({ success: false, message: 'Only verified transactions can be refunded' });
    }
    const amount = Number(req.body.amount);
    if (amount + Number(transaction.refunded) > Number(transaction.gross_amount)) {
      return res.status(400).json({ success: false, message: 'Refund exceeds remaining transaction amount' });
    }
    const [result] = await pool.query(
      'INSERT INTO payment_refunds (transaction_id,amount,reason,requested_by) VALUES (?,?,?,?)',
      [req.body.transaction_id, amount, req.body.reason, req.user.id]
    );
    await log(pool, req.user.id, 'REQUEST_REFUND', 'payment_refund', result.insertId, { amount });
    return res.status(201).json({ success: true, message: 'Refund requested', data: { id: result.insertId } });
  } catch (error) { return fail(res, error, 'Failed to create refund'); }
};

const updateRefund = async (req, res) => {
  if (invalid(req, res)) return;
  const next = req.body.status.toUpperCase();
  const db = await getPool().getConnection();
  try {
    await db.beginTransaction();
    const [rows] = await db.query('SELECT * FROM payment_refunds WHERE id=? FOR UPDATE', [req.params.id]);
    if (!rows.length) { await db.rollback(); return res.status(404).json({ success: false, message: 'Refund not found' }); }
    const transitions = { REQUESTED: ['APPROVED','REJECTED'], APPROVED: ['COMPLETED','REJECTED'], COMPLETED: [], REJECTED: [] };
    if (!transitions[rows[0].status].includes(next)) {
      await db.rollback();
      return res.status(409).json({ success: false, message: `Cannot change ${rows[0].status} refund to ${next}` });
    }
    if (next === 'COMPLETED' && !req.body.refund_reference) {
      await db.rollback();
      return res.status(400).json({ success: false, message: 'Refund reference is required' });
    }
    await db.query(
      `UPDATE payment_refunds SET status=?,approved_by=?,approved_at=IF(?='APPROVED',NOW(),approved_at),
       completed_at=IF(?='COMPLETED',NOW(),completed_at),refund_reference=COALESCE(?,refund_reference) WHERE id=?`,
      [next, req.user.id, next, next, req.body.refund_reference || null, req.params.id]
    );
    if (next === 'COMPLETED') {
      const [[totals]] = await db.query(
        `SELECT pt.gross_amount,COALESCE(SUM(pr.amount),0) refunded FROM payment_transactions pt
         LEFT JOIN payment_refunds pr ON pr.transaction_id=pt.id AND pr.status='COMPLETED'
         WHERE pt.id=? GROUP BY pt.id`, [rows[0].transaction_id]
      );
      await db.query('UPDATE payment_transactions SET payment_status=? WHERE id=?',
        [Number(totals.refunded) >= Number(totals.gross_amount) ? 'REFUNDED' : 'PARTIALLY_REFUNDED', rows[0].transaction_id]);
    }
    await log(db, req.user.id, `REFUND_${next}`, 'payment_refund', req.params.id);
    await db.commit();
    return res.json({ success: true, message: `Refund marked ${next.toLowerCase()}` });
  } catch (error) {
    await db.rollback();
    return fail(res, error, 'Failed to update refund');
  } finally { db.release(); }
};

module.exports = { getDashboard, getTransactions, createTransaction, verifyTransaction,
  getPayouts, updatePayout, getSettlements, createSettlement, updateSettlement,
  getMonthlyReport, getRefunds, createRefund, updateRefund };
