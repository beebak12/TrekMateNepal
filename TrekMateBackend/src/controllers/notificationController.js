const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const getNotifications = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query(
      'SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC',
      [req.user.id]
    );

    return res.status(200).json({
      success: true,
      data: rows,
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch notifications',
    });
  }
};

const markNotificationAsRead = async (req, res) => {
  try {
    const pool = getPool();
    const [result] = await pool.query(
      'UPDATE notifications SET is_read = 1 WHERE id = ? AND user_id = ?',
      [req.params.id, req.user.id]
    );

    if (result.affectedRows === 0) {
      return res.status(404).json({
        success: false,
        message: 'Notification not found',
      });
    }

    return res.status(200).json({
      success: true,
      message: 'Notification marked as read',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to update notification',
    });
  }
};

const createNotification = async (req, res) => {
  const errors = validationResult(req);

  if (!errors.isEmpty()) {
    return res.status(400).json({
      success: false,
      message: 'Validation failed',
      errors: errors.array(),
    });
  }

  try {
    const pool = getPool();
    const { user_id, title, message, type, related_id } = req.body;

    const [result] = await pool.query(
      `INSERT INTO notifications (user_id, title, message, type, related_id)
       VALUES (?, ?, ?, ?, ?)`,
      [user_id, title, message, type || 'general', related_id || null]
    );

    return res.status(201).json({
      success: true,
      message: 'Notification created successfully',
      data: {
        id: result.insertId,
        user_id,
        title,
        message,
        type: type || 'general',
        related_id: related_id || null,
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to create notification',
    });
  }
};

module.exports = {
  getNotifications,
  markNotificationAsRead,
  createNotification,
};
