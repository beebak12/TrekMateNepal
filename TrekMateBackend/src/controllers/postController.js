const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const getPosts = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM community_posts ORDER BY created_at DESC');

    return res.status(200).json({
      success: true,
      data: rows,
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch community posts',
    });
  }
};

const getPostById = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM community_posts WHERE id = ?', [req.params.id]);

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Post not found',
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
      message: 'Failed to fetch post',
    });
  }
};

const createPost = async (req, res) => {
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
    const { title, content, image_url } = req.body;

    const [result] = await pool.query(
      'INSERT INTO community_posts (user_id, title, content, image_url) VALUES (?, ?, ?, ?)',
      [req.user.id, title || null, content, image_url || null]
    );

    return res.status(201).json({
      success: true,
      message: 'Post created successfully',
      data: { id: result.insertId },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to create post',
    });
  }
};

const updatePost = async (req, res) => {
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
    const [rows] = await pool.query('SELECT * FROM community_posts WHERE id = ?', [req.params.id]);

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Post not found',
      });
    }

    if (rows[0].user_id !== req.user.id && req.user.role_id !== 3) {
      return res.status(403).json({
        success: false,
        message: 'You are not allowed to update this post',
      });
    }

    const { title, content, image_url } = req.body;

    await pool.query(
      'UPDATE community_posts SET title = ?, content = ?, image_url = ? WHERE id = ?',
      [title ?? rows[0].title, content ?? rows[0].content, image_url ?? rows[0].image_url, req.params.id]
    );

    return res.status(200).json({
      success: true,
      message: 'Post updated successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to update post',
    });
  }
};

const deletePost = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM community_posts WHERE id = ?', [req.params.id]);

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Post not found',
      });
    }

    if (rows[0].user_id !== req.user.id && req.user.role_id !== 3) {
      return res.status(403).json({
        success: false,
        message: 'You are not allowed to delete this post',
      });
    }

    await pool.query('DELETE FROM community_posts WHERE id = ?', [req.params.id]);

    return res.status(200).json({
      success: true,
      message: 'Post deleted successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to delete post',
    });
  }
};

module.exports = {
  getPosts,
  getPostById,
  createPost,
  updatePost,
  deletePost,
};
