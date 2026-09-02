const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const getGuides = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM guides ORDER BY created_at DESC');

    return res.status(200).json({
      success: true,
      data: rows,
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch guides',
    });
  }
};

const getGuideById = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM guides WHERE id = ?', [req.params.id]);

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Guide not found',
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
      message: 'Failed to fetch guide',
    });
  }
};

const createGuide = async (req, res) => {
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
      user_id,
      full_name,
      license_number,
      experience_years,
      languages,
      specializations,
      location,
      description,
      phone,
      profile_image,
      verification_status,
      rating,
      availability,
    } = req.body;

    const [result] = await pool.query(
      `INSERT INTO guides (
        user_id, full_name, license_number, experience_years, languages,
        specializations, location, description, phone, profile_image,
        verification_status, rating, availability
      ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`,
      [
        user_id || null,
        full_name,
        license_number,
        Number(experience_years),
        languages || null,
        specializations || null,
        location || null,
        description || null,
        phone || null,
        profile_image || null,
        verification_status || 'pending',
        Number(rating || 0),
        availability || 'available',
      ]
    );

    return res.status(201).json({
      success: true,
      message: 'Guide created successfully',
      data: { id: result.insertId },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to create guide',
    });
  }
};

const updateGuide = async (req, res) => {
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
    const [rows] = await pool.query('SELECT * FROM guides WHERE id = ?', [req.params.id]);

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Guide not found',
      });
    }

    const {
      full_name,
      license_number,
      experience_years,
      languages,
      specializations,
      location,
      description,
      phone,
      profile_image,
      verification_status,
      rating,
      availability,
    } = req.body;

    await pool.query(
      `UPDATE guides
       SET full_name = ?, license_number = ?, experience_years = ?, languages = ?,
           specializations = ?, location = ?, description = ?, phone = ?, profile_image = ?,
           verification_status = ?, rating = ?, availability = ?
       WHERE id = ?`,
      [
        full_name ?? rows[0].full_name,
        license_number ?? rows[0].license_number,
        Number(experience_years ?? rows[0].experience_years),
        languages ?? rows[0].languages,
        specializations ?? rows[0].specializations,
        location ?? rows[0].location,
        description ?? rows[0].description,
        phone ?? rows[0].phone,
        profile_image ?? rows[0].profile_image,
        verification_status ?? rows[0].verification_status,
        Number(rating ?? rows[0].rating),
        availability ?? rows[0].availability,
        req.params.id,
      ]
    );

    return res.status(200).json({
      success: true,
      message: 'Guide updated successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to update guide',
    });
  }
};

const deleteGuide = async (req, res) => {
  try {
    const pool = getPool();
    const [result] = await pool.query('DELETE FROM guides WHERE id = ?', [req.params.id]);

    if (result.affectedRows === 0) {
      return res.status(404).json({
        success: false,
        message: 'Guide not found',
      });
    }

    return res.status(200).json({
      success: true,
      message: 'Guide deleted successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to delete guide',
    });
  }
};

module.exports = {
  getGuides,
  getGuideById,
  createGuide,
  updateGuide,
  deleteGuide,
};
