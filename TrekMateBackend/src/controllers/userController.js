const bcrypt = require('bcryptjs');
const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const getProfile = async (req, res) => {
  try {
    const pool = getPool();

    const [userRows] = await pool.query(
      `SELECT u.id, u.full_name, u.username, u.email, u.phone, u.dob, u.gender, u.profile_image,
              p.bio, p.city, p.country, p.social_links
       FROM users u
       LEFT JOIN user_profiles p ON p.user_id = u.id
       WHERE u.id = ?`,
      [req.user.id]
    );

    if (!userRows.length) {
      return res.status(404).json({
        success: false,
        message: 'User profile not found',
      });
    }

    const profile = userRows[0];

    return res.status(200).json({
      success: true,
      data: {
        id: profile.id,
        full_name: profile.full_name,
        username: profile.username,
        email: profile.email,
        phone: profile.phone,
        dob: profile.dob,
        gender: profile.gender,
        profile_image: profile.profile_image,
        bio: profile.bio,
        city: profile.city,
        country: profile.country,
        social_links: profile.social_links || [],
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch profile',
    });
  }
};

const updateProfile = async (req, res) => {
  const errors = validationResult(req);

  if (!errors.isEmpty()) {
    return res.status(400).json({
      success: false,
      message: 'Validation failed',
      errors: errors.array(),
    });
  }

  const { full_name, username, email, phone, dob, gender, bio, city, country, social_links } = req.body;

  try {
    const pool = getPool();

    const [conflictRows] = await pool.query(
      'SELECT id FROM users WHERE (email = ? OR username = ?) AND id != ?',
      [email, username, req.user.id]
    );

    if (conflictRows.length > 0) {
      return res.status(409).json({
        success: false,
        message: 'Username or email already in use',
      });
    }

    await pool.query(
      `UPDATE users
       SET full_name = ?, username = ?, email = ?, phone = ?, dob = ?, gender = ?
       WHERE id = ?`,
      [full_name, username, email, phone || null, dob || null, gender || null, req.user.id]
    );

    await pool.query(
      `INSERT INTO user_profiles (user_id, bio, city, country, social_links)
       VALUES (?, ?, ?, ?, ?)
       ON DUPLICATE KEY UPDATE
         bio = VALUES(bio),
         city = VALUES(city),
         country = VALUES(country),
         social_links = VALUES(social_links)`,
      [
        req.user.id,
        bio || null,
        city || null,
        country || null,
        social_links ? JSON.stringify(social_links) : null,
      ]
    );

    const [updatedRows] = await pool.query(
      `SELECT u.id, u.full_name, u.username, u.email, u.phone, u.dob, u.gender, u.profile_image,
              p.bio, p.city, p.country, p.social_links
       FROM users u
       LEFT JOIN user_profiles p ON p.user_id = u.id
       WHERE u.id = ?`,
      [req.user.id]
    );

    const profile = updatedRows[0];

    return res.status(200).json({
      success: true,
      message: 'Profile updated successfully',
      data: {
        id: profile.id,
        full_name: profile.full_name,
        username: profile.username,
        email: profile.email,
        phone: profile.phone,
        dob: profile.dob,
        gender: profile.gender,
        profile_image: profile.profile_image,
        bio: profile.bio,
        city: profile.city,
        country: profile.country,
        social_links: profile.social_links || [],
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to update profile',
    });
  }
};

const changePassword = async (req, res) => {
  const errors = validationResult(req);

  if (!errors.isEmpty()) {
    return res.status(400).json({
      success: false,
      message: 'Validation failed',
      errors: errors.array(),
    });
  }

  const { current_password, new_password } = req.body;

  try {
    const pool = getPool();
    const [rows] = await pool.query(
      'SELECT password_hash FROM users WHERE id = ?',
      [req.user.id]
    );

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'User not found',
      });
    }

    const isMatch = await bcrypt.compare(current_password, rows[0].password_hash);

    if (!isMatch) {
      return res.status(400).json({
        success: false,
        message: 'Current password is incorrect',
      });
    }

    const newHash = await bcrypt.hash(new_password, 10);
    await pool.query('UPDATE users SET password_hash = ? WHERE id = ?', [newHash, req.user.id]);

    return res.status(200).json({
      success: true,
      message: 'Password changed successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to change password',
    });
  }
};

const uploadProfileImageController = async (req, res) => {
  if (!req.file) {
    return res.status(400).json({
      success: false,
      message: 'Profile image is required',
    });
  }

  try {
    const pool = getPool();
    const imageUrl = `/uploads/profiles/${req.file.filename}`;

    await pool.query('UPDATE users SET profile_image = ? WHERE id = ?', [imageUrl, req.user.id]);

    return res.status(200).json({
      success: true,
      message: 'Profile image uploaded successfully',
      data: {
        profile_image: imageUrl,
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to upload profile image',
    });
  }
};

const deleteAccount = async (req, res) => {
  try {
    const pool = getPool();
    await pool.query('DELETE FROM users WHERE id = ?', [req.user.id]);

    return res.status(200).json({
      success: true,
      message: 'Account deleted successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to delete account',
    });
  }
};

module.exports = {
  getProfile,
  updateProfile,
  changePassword,
  uploadProfileImageController,
  deleteAccount,
};
