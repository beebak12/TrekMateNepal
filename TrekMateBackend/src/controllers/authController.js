const bcrypt = require('bcryptjs');
const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');
const { generateToken } = require('../utils/generateToken');

const registerUser = async (req, res) => {
  const errors = validationResult(req);

  if (!errors.isEmpty()) {
    return res.status(400).json({
      success: false,
      message: 'Validation failed',
      errors: errors.array(),
    });
  }

  const { full_name, username, email, phone, password, dob, gender } = req.body;

  try {
    const pool = getPool();

    const [existingUser] = await pool.query(
      'SELECT id FROM users WHERE email = ? OR username = ?',
      [email, username]
    );

    if (existingUser.length > 0) {
      return res.status(409).json({
        success: false,
        message: 'User with this email or username already exists',
      });
    }

    const passwordHash = await bcrypt.hash(password, 10);

    const [result] = await pool.query(
      `INSERT INTO users (role_id, full_name, username, email, phone, password_hash, dob, gender)
       VALUES (1, ?, ?, ?, ?, ?, ?, ?)`,
      [full_name, username, email, phone, passwordHash, dob || null, gender || null]
    );

    const [userRows] = await pool.query(
      'SELECT id, full_name, username, email, phone, dob, gender, role_id FROM users WHERE id = ?',
      [result.insertId]
    );

    const user = userRows[0];
    const token = generateToken(user);

    return res.status(201).json({
      success: true,
      message: 'Registration successful',
      token,
      user: {
        id: user.id,
        full_name: user.full_name,
        username: user.username,
        email: user.email,
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Registration failed',
    });
  }
};

const loginUser = async (req, res) => {
  const errors = validationResult(req);

  if (!errors.isEmpty()) {
    return res.status(400).json({
      success: false,
      message: 'Validation failed',
      errors: errors.array(),
    });
  }

  const { email, password } = req.body;

  try {
    const pool = getPool();
    const [rows] = await pool.query(
      'SELECT id, full_name, username, email, password_hash, role_id FROM users WHERE email = ?',
      [email]
    );

    if (!rows.length) {
      return res.status(401).json({
        success: false,
        message: 'Invalid email or password',
      });
    }

    const user = rows[0];
    const isMatch = await bcrypt.compare(password, user.password_hash);

    if (!isMatch) {
      return res.status(401).json({
        success: false,
        message: 'Invalid email or password',
      });
    }

    const token = generateToken(user);

    return res.status(200).json({
      success: true,
      message: 'Login successful',
      token,
      user: {
        id: user.id,
        full_name: user.full_name,
        username: user.username,
        email: user.email,
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Login failed',
    });
  }
};

const getCurrentUser = async (req, res) => {
  return res.status(200).json({
    success: true,
    user: {
      id: req.user.id,
      full_name: req.user.full_name,
      username: req.user.username,
      email: req.user.email,
    },
  });
};

const forgotPassword = async (req, res) => {
  return res.status(200).json({
    success: true,
    message: 'Forgot password endpoint is ready for implementation',
  });
};

const resetPassword = async (req, res) => {
  return res.status(200).json({
    success: true,
    message: 'Reset password endpoint is ready for implementation',
  });
};

module.exports = {
  registerUser,
  loginUser,
  getCurrentUser,
  forgotPassword,
  resetPassword,
};
