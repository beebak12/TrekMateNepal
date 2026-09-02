const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const getReviews = async (req, res) => {
  try {
    const { target_type, target_id } = req.query;
    const pool = getPool();

    let query = 'SELECT * FROM reviews';
    const params = [];

    if (target_type || target_id) {
      const conditions = [];

      if (target_type) {
        conditions.push('target_type = ?');
        params.push(target_type);
      }

      if (target_id) {
        conditions.push('target_id = ?');
        params.push(target_id);
      }

      query += ` WHERE ${conditions.join(' AND ')}`;
    }

    query += ' ORDER BY created_at DESC';

    const [rows] = await pool.query(query, params);

    return res.status(200).json({
      success: true,
      data: rows,
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch reviews',
    });
  }
};

const getReviewById = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM reviews WHERE id = ?', [req.params.id]);

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Review not found',
      });
    }

    return res.status(200).json({
      success: true,
      data: rows[0],
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch review',
    });
  }
};

const createReview = async (req, res) => {
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
    const { target_type, target_id, rating, review } = req.body;

    const [result] = await pool.query(
      `INSERT INTO reviews (user_id, target_type, target_id, rating, review)
       VALUES (?, ?, ?, ?, ?)`,
      [req.user.id, target_type, target_id, Number(rating), review || null]
    );

    return res.status(201).json({
      success: true,
      message: 'Review created successfully',
      data: {
        id: result.insertId,
        user_id: req.user.id,
        target_type,
        target_id,
        rating: Number(rating),
        review: review || null,
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to create review',
    });
  }
};

const updateReview = async (req, res) => {
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
    const [rows] = await pool.query('SELECT * FROM reviews WHERE id = ? AND user_id = ?', [req.params.id, req.user.id]);

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Review not found or not owned by user',
      });
    }

    const { rating, review } = req.body;

    await pool.query(
      'UPDATE reviews SET rating = ?, review = ? WHERE id = ?',
      [Number(rating ?? rows[0].rating), review ?? rows[0].review, req.params.id]
    );

    return res.status(200).json({
      success: true,
      message: 'Review updated successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to update review',
    });
  }
};

const deleteReview = async (req, res) => {
  try {
    const pool = getPool();
    const [result] = await pool.query('DELETE FROM reviews WHERE id = ? AND user_id = ?', [req.params.id, req.user.id]);

    if (result.affectedRows === 0) {
      return res.status(404).json({
        success: false,
        message: 'Review not found or not owned by user',
      });
    }

    return res.status(200).json({
      success: true,
      message: 'Review deleted successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to delete review',
    });
  }
};

module.exports = {
  getReviews,
  getReviewById,
  createReview,
  updateReview,
  deleteReview,
};
