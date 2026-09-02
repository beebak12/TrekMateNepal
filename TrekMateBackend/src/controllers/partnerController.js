const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const getPartnerPosts = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM partner_posts ORDER BY created_at DESC');

    return res.status(200).json({
      success: true,
      data: rows,
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch partner posts',
    });
  }
};

const getPartnerPostById = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM partner_posts WHERE id = ?', [req.params.id]);

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Partner post not found',
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
      message: 'Failed to fetch partner post',
    });
  }
};

const createPartnerPost = async (req, res) => {
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
    const {
      trek_id,
      required_partners,
      travel_date,
      expected_duration,
      experience_level,
      gender_preference,
      description,
    } = req.body;

    const [result] = await pool.query(
      `INSERT INTO partner_posts (
        user_id, trek_id, required_partners, travel_date, expected_duration,
        experience_level, gender_preference, description, status
      ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'open')`,
      [
        req.user.id,
        trek_id || null,
        Number(required_partners),
        travel_date,
        expected_duration || null,
        experience_level || 'Any',
        gender_preference || 'Any',
        description || null,
      ]
    );

    return res.status(201).json({
      success: true,
      message: 'Partner post created successfully',
      data: { id: result.insertId },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to create partner post',
    });
  }
};

const updatePartnerPost = async (req, res) => {
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
    const [rows] = await pool.query('SELECT * FROM partner_posts WHERE id = ?', [req.params.id]);

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Partner post not found',
      });
    }

    if (rows[0].user_id !== req.user.id && req.user.role_id !== 3) {
      return res.status(403).json({
        success: false,
        message: 'You are not allowed to update this post',
      });
    }

    const {
      trek_id,
      required_partners,
      travel_date,
      expected_duration,
      experience_level,
      gender_preference,
      description,
      status,
    } = req.body;

    await pool.query(
      `UPDATE partner_posts
       SET trek_id = ?, required_partners = ?, travel_date = ?, expected_duration = ?,
           experience_level = ?, gender_preference = ?, description = ?, status = ?
       WHERE id = ?`,
      [
        trek_id ?? rows[0].trek_id,
        Number(required_partners ?? rows[0].required_partners),
        travel_date ?? rows[0].travel_date,
        expected_duration ?? rows[0].expected_duration,
        experience_level ?? rows[0].experience_level,
        gender_preference ?? rows[0].gender_preference,
        description ?? rows[0].description,
        status ?? rows[0].status,
        req.params.id,
      ]
    );

    return res.status(200).json({
      success: true,
      message: 'Partner post updated successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to update partner post',
    });
  }
};

const deletePartnerPost = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM partner_posts WHERE id = ?', [req.params.id]);

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Partner post not found',
      });
    }

    if (rows[0].user_id !== req.user.id && req.user.role_id !== 3) {
      return res.status(403).json({
        success: false,
        message: 'You are not allowed to delete this post',
      });
    }

    await pool.query('DELETE FROM partner_posts WHERE id = ?', [req.params.id]);

    return res.status(200).json({
      success: true,
      message: 'Partner post deleted successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to delete partner post',
    });
  }
};

const requestPartner = async (req, res) => {
  try {
    const pool = getPool();
    const [postRows] = await pool.query('SELECT * FROM partner_posts WHERE id = ?', [req.params.id]);

    if (!postRows.length) {
      return res.status(404).json({
        success: false,
        message: 'Partner post not found',
      });
    }

    const [existingRows] = await pool.query(
      'SELECT id FROM partner_requests WHERE partner_post_id = ? AND requester_id = ?',
      [req.params.id, req.user.id]
    );

    if (existingRows.length > 0) {
      return res.status(409).json({
        success: false,
        message: 'You have already requested to join this trek',
      });
    }

    const [result] = await pool.query(
      'INSERT INTO partner_requests (partner_post_id, requester_id, message, status) VALUES (?, ?, ?, "pending")',
      [req.params.id, req.user.id, req.body.message || null]
    );

    return res.status(201).json({
      success: true,
      message: 'Partner request sent successfully',
      data: { id: result.insertId },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to send partner request',
    });
  }
};

module.exports = {
  getPartnerPosts,
  getPartnerPostById,
  createPartnerPost,
  updatePartnerPost,
  deletePartnerPost,
  requestPartner,
};
