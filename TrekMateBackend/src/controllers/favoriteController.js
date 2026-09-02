const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const getFavorites = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query(
      'SELECT * FROM favorites WHERE user_id = ? ORDER BY created_at DESC',
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
      message: 'Failed to fetch favorites',
    });
  }
};

const addFavorite = async (req, res) => {
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
    const { entity_type, entity_id } = req.body;

    const [existing] = await pool.query(
      'SELECT * FROM favorites WHERE user_id = ? AND entity_type = ? AND entity_id = ?',
      [req.user.id, entity_type, entity_id]
    );

    if (existing.length) {
      return res.status(400).json({
        success: false,
        message: 'Favorite already exists',
      });
    }

    const [result] = await pool.query(
      'INSERT INTO favorites (user_id, entity_type, entity_id) VALUES (?, ?, ?)',
      [req.user.id, entity_type, entity_id]
    );

    return res.status(201).json({
      success: true,
      message: 'Favorite added successfully',
      data: {
        id: result.insertId,
        user_id: req.user.id,
        entity_type,
        entity_id,
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to add favorite',
    });
  }
};

const removeFavorite = async (req, res) => {
  try {
    const pool = getPool();
    const { entity_type, entity_id } = req.params;

    const [result] = await pool.query(
      'DELETE FROM favorites WHERE user_id = ? AND entity_type = ? AND entity_id = ?',
      [req.user.id, entity_type, entity_id]
    );

    if (result.affectedRows === 0) {
      return res.status(404).json({
        success: false,
        message: 'Favorite not found',
      });
    }

    return res.status(200).json({
      success: true,
      message: 'Favorite removed successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to remove favorite',
    });
  }
};

module.exports = {
  getFavorites,
  addFavorite,
  removeFavorite,
};
