const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const getAdminLogs = async (req, res) => {
  try {
    const [rows] = await getPool().query('SELECT * FROM admin_logs ORDER BY created_at DESC LIMIT 200');
    return res.status(200).json({ success: true, data: rows });
  } catch (error) {
    console.error(error);
    return res.status(500).json({ success: false, message: 'Failed to fetch admin logs' });
  }
};

const logAdminAction = async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, message: 'Validation failed', errors: errors.array() });
  }
  try {
    const { action, entity_type, entity_id, details } = req.body;
    const [result] = await getPool().query(
      `INSERT INTO admin_logs (user_id, action, entity_type, entity_id, details) VALUES (?, ?, ?, ?, ?)`,
      [req.user.id, action, entity_type || null, entity_id || null, JSON.stringify(details || {})]
    );
    return res.status(201).json({ success: true, message: 'Admin log created successfully', data: { id: result.insertId } });
  } catch (error) {
    console.error(error);
    return res.status(500).json({ success: false, message: 'Failed to log admin action' });
  }
};

module.exports = { getAdminLogs, logAdminAction };
