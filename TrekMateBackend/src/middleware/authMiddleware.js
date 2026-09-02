const jwt = require('jsonwebtoken');
const { getPool } = require('../config/db');

const protect = async (req, res, next) => {
  try {
    const authHeader = req.headers.authorization;

    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(401).json({
        success: false,
        message: 'Authentication required',
      });
    }

    const token = authHeader.split(' ')[1];
    const decoded = jwt.verify(token, process.env.JWT_SECRET || 'trekmate-secret');

    const pool = getPool();
    const [rows] = await pool.query(
      'SELECT id, full_name, username, email, role_id, is_active FROM users WHERE id = ?',
      [decoded.id]
    );

    if (!rows.length) {
      return res.status(401).json({
        success: false,
        message: 'User not found',
      });
    }

    if (!rows[0].is_active) {
      return res.status(403).json({
        success: false,
        message: 'Account is deactivated',
      });
    }

    req.user = rows[0];
    next();
  } catch (error) {
    return res.status(401).json({
      success: false,
      message: 'Invalid or expired token',
    });
  }
};

const authorize = (...roles) => {
  return (req, res, next) => {
    if (!req.user) {
      return res.status(401).json({
        success: false,
        message: 'Authentication required',
      });
    }

    const userRole = req.user.role_id || 1;
    const allowedRoles = roles.map((role) => String(role));

    if (!allowedRoles.includes(String(userRole))) {
      return res.status(403).json({
        success: false,
        message: 'Access forbidden',
      });
    }

    next();
  };
};

module.exports = { protect, authorize };
