const bcrypt = require('bcryptjs');
const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

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

const log = (db, adminId, action, id, details = {}) => db.query(
  `INSERT INTO admin_logs (user_id,action,entity_type,entity_id,details) VALUES (?,?,'user',?,?)`,
  [adminId, action, id, JSON.stringify(details)]
);

const getUsers = async (req, res) => {
  try {
    const search = String(req.query.search || '').trim();
    const role = String(req.query.role || '').trim().toUpperCase();
    const status = String(req.query.status || '').trim().toLowerCase();
    const where = [];
    const values = [];
    if (search) {
      where.push('(u.full_name LIKE ? OR u.username LIKE ? OR u.email LIKE ?)');
      values.push(`%${search}%`, `%${search}%`, `%${search}%`);
    }
    if (role) { where.push('r.name=?'); values.push(role); }
    if (['active','deactivated'].includes(status)) { where.push('u.is_active=?'); values.push(status === 'active' ? 1 : 0); }
    const [rows] = await getPool().query(
      `SELECT u.id,u.full_name,u.username,u.email,u.phone,u.dob,u.gender,u.is_active,
              u.is_email_verified,u.created_at,r.name role,
              COALESCE(gs.gear_listed,0) gear_listed,
              COALESCE(bs.gear_booked,0) gear_booked,
              COALESCE(es.provider_earnings,0) provider_earnings
       FROM users u JOIN roles r ON r.id=u.role_id
       LEFT JOIN (SELECT owner_user_id,COUNT(*) gear_listed FROM gear
                  WHERE owner_user_id IS NOT NULL GROUP BY owner_user_id) gs ON gs.owner_user_id=u.id
       LEFT JOIN (SELECT g.owner_user_id,SUM(ri.quantity) gear_booked FROM rental_items ri
                  JOIN gear g ON g.id=ri.gear_id JOIN gear_rentals gr ON gr.id=ri.rental_id
                  WHERE g.owner_user_id IS NOT NULL AND gr.status IN ('confirmed','completed')
                  GROUP BY g.owner_user_id) bs ON bs.owner_user_id=u.id
       LEFT JOIN (SELECT provider_id,SUM(amount) provider_earnings FROM provider_payouts
                  WHERE status='PAID' GROUP BY provider_id) es ON es.provider_id=u.id
       ${where.length ? `WHERE ${where.join(' AND ')}` : ''} ORDER BY u.created_at DESC`, values
    );
    return res.json({ success: true, data: rows });
  } catch (error) { return fail(res, error, 'Failed to fetch users'); }
};

const createUser = async (req, res) => {
  if (invalid(req, res)) return;
  const { full_name, username, email, phone, password, role_id = 1, dob, gender } = req.body;
  try {
    const pool = getPool();
    const [existing] = await pool.query('SELECT id FROM users WHERE email=? OR username=?', [email, username]);
    if (existing.length) return res.status(409).json({ success: false, message: 'Email or username already exists' });
    const passwordHash = await bcrypt.hash(password, 10);
    const [result] = await pool.query(
      `INSERT INTO users (role_id,full_name,username,email,phone,password_hash,dob,gender) VALUES (?,?,?,?,?,?,?,?)`,
      [Number(role_id), full_name, username, email, phone || null, passwordHash, dob || null, gender || null]
    );
    await log(pool, req.user.id, 'CREATE_USER', result.insertId, { email, role_id: Number(role_id) });
    return res.status(201).json({ success: true, message: 'User created', data: { id: result.insertId } });
  } catch (error) { return fail(res, error, 'Failed to create user'); }
};

const updateUser = async (req, res) => {
  if (invalid(req, res)) return;
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM users WHERE id=?', [req.params.id]);
    if (!rows.length) return res.status(404).json({ success: false, message: 'User not found' });
    const old = rows[0];
    const data = {
      full_name: req.body.full_name ?? old.full_name,
      username: req.body.username ?? old.username,
      email: req.body.email ?? old.email,
      phone: req.body.phone ?? old.phone,
      role_id: Number(req.body.role_id ?? old.role_id),
      dob: req.body.dob ?? old.dob,
      gender: req.body.gender ?? old.gender,
    };
    const [conflict] = await pool.query('SELECT id FROM users WHERE (email=? OR username=?) AND id<>?',
      [data.email, data.username, req.params.id]);
    if (conflict.length) return res.status(409).json({ success: false, message: 'Email or username already exists' });
    await pool.query(
      `UPDATE users SET full_name=?,username=?,email=?,phone=?,role_id=?,dob=?,gender=? WHERE id=?`,
      [data.full_name, data.username, data.email, data.phone, data.role_id, data.dob, data.gender, req.params.id]
    );
    await log(pool, req.user.id, 'UPDATE_USER', req.params.id, { fields: Object.keys(req.body) });
    return res.json({ success: true, message: 'User updated' });
  } catch (error) { return fail(res, error, 'Failed to update user'); }
};

const setUserStatus = async (req, res) => {
  if (invalid(req, res)) return;
  const active = req.body.is_active === true;
  if (!active && Number(req.params.id) === Number(req.user.id)) {
    return res.status(400).json({ success: false, message: 'You cannot deactivate your own admin account' });
  }
  try {
    const pool = getPool();
    const [result] = await pool.query('UPDATE users SET is_active=? WHERE id=?', [active ? 1 : 0, req.params.id]);
    if (!result.affectedRows) return res.status(404).json({ success: false, message: 'User not found' });
    await log(pool, req.user.id, active ? 'ACTIVATE_USER' : 'DEACTIVATE_USER', req.params.id);
    return res.json({ success: true, message: `User ${active ? 'activated' : 'deactivated'}` });
  } catch (error) { return fail(res, error, 'Failed to change user status'); }
};

const getProviders = async (req, res) => {
  try {
    const [rows] = await getPool().query(
      `SELECT u.id,u.full_name,u.username,u.email,u.phone,u.is_active,
              COUNT(DISTINCT g.id) gear_listed,
              COALESCE(SUM(CASE WHEN gr.status IN ('confirmed','completed') THEN ri.quantity ELSE 0 END),0) gear_booked,
              COALESCE(pe.earnings,0) provider_earnings
       FROM users u JOIN gear g ON g.owner_user_id=u.id
       LEFT JOIN rental_items ri ON ri.gear_id=g.id LEFT JOIN gear_rentals gr ON gr.id=ri.rental_id
       LEFT JOIN (SELECT provider_id,SUM(amount) earnings FROM provider_payouts
                  WHERE status='PAID' GROUP BY provider_id) pe ON pe.provider_id=u.id
       GROUP BY u.id,pe.earnings ORDER BY u.full_name`
    );
    return res.json({ success: true, data: rows });
  } catch (error) { return fail(res, error, 'Failed to fetch providers'); }
};

const getProviderHistory = async (req, res) => {
  try {
    const pool = getPool();
    const [users] = await pool.query('SELECT id,full_name,username,email,phone,is_active FROM users WHERE id=?', [req.params.id]);
    if (!users.length) return res.status(404).json({ success: false, message: 'Provider not found' });
    const [gear] = await pool.query(
      `SELECT g.id,g.name,g.price_per_day,g.quantity,g.availability,
              COALESCE(SUM(CASE WHEN gr.status IN ('confirmed','completed') THEN ri.quantity ELSE 0 END),0) times_booked
       FROM gear g LEFT JOIN rental_items ri ON ri.gear_id=g.id
       LEFT JOIN gear_rentals gr ON gr.id=ri.rental_id WHERE g.owner_user_id=? GROUP BY g.id`, [req.params.id]
    );
    const [transactions] = await pool.query(
      `SELECT pt.*,u.full_name customer_name,pp.status payout_status FROM payment_transactions pt
       JOIN users u ON u.id=pt.customer_id LEFT JOIN provider_payouts pp ON pp.transaction_id=pt.id
       WHERE pt.provider_id=? ORDER BY pt.created_at DESC`, [req.params.id]
    );
    return res.json({ success: true, data: { provider: users[0], gear, transactions } });
  } catch (error) { return fail(res, error, 'Failed to fetch provider history'); }
};

module.exports = { getUsers, createUser, updateUser, setUserStatus, getProviders, getProviderHistory };
