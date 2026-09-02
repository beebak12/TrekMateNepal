const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const getEmergencyContacts = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM emergency_contacts ORDER BY type, name');

    return res.status(200).json({
      success: true,
      data: rows,
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch emergency contacts',
    });
  }
};

const getEmergencyContactById = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM emergency_contacts WHERE id = ?', [req.params.id]);

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Emergency contact not found',
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
      message: 'Failed to fetch emergency contact',
    });
  }
};

const createEmergencyContact = async (req, res) => {
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
    const { name, type, phone, location, description, latitude, longitude } = req.body;

    const [result] = await pool.query(
      `INSERT INTO emergency_contacts (name, type, phone, location, description, latitude, longitude)
       VALUES (?, ?, ?, ?, ?, ?, ?)`,
      [name, type, phone, location || null, description || null, latitude || null, longitude || null]
    );

    return res.status(201).json({
      success: true,
      message: 'Emergency contact created successfully',
      data: {
        id: result.insertId,
        name,
        type,
        phone,
        location: location || null,
        description: description || null,
        latitude: latitude || null,
        longitude: longitude || null,
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to create emergency contact',
    });
  }
};

const updateEmergencyContact = async (req, res) => {
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
    const [rows] = await pool.query('SELECT * FROM emergency_contacts WHERE id = ?', [req.params.id]);

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Emergency contact not found',
      });
    }

    const { name, type, phone, location, description, latitude, longitude } = req.body;

    await pool.query(
      `UPDATE emergency_contacts
       SET name = ?, type = ?, phone = ?, location = ?, description = ?, latitude = ?, longitude = ?
       WHERE id = ?`,
      [
        name ?? rows[0].name,
        type ?? rows[0].type,
        phone ?? rows[0].phone,
        location ?? rows[0].location,
        description ?? rows[0].description,
        latitude ?? rows[0].latitude,
        longitude ?? rows[0].longitude,
        req.params.id,
      ]
    );

    return res.status(200).json({
      success: true,
      message: 'Emergency contact updated successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to update emergency contact',
    });
  }
};

const deleteEmergencyContact = async (req, res) => {
  try {
    const pool = getPool();
    const [result] = await pool.query('DELETE FROM emergency_contacts WHERE id = ?', [req.params.id]);

    if (result.affectedRows === 0) {
      return res.status(404).json({
        success: false,
        message: 'Emergency contact not found',
      });
    }

    return res.status(200).json({
      success: true,
      message: 'Emergency contact deleted successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to delete emergency contact',
    });
  }
};

module.exports = {
  getEmergencyContacts,
  getEmergencyContactById,
  createEmergencyContact,
  updateEmergencyContact,
  deleteEmergencyContact,
};
