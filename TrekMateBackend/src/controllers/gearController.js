const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const getGears = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM gear ORDER BY created_at DESC');

    return res.status(200).json({
      success: true,
      data: rows,
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch gear items',
    });
  }
};

const getGearById = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM gear WHERE id = ?', [req.params.id]);

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Gear item not found',
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
      message: 'Failed to fetch gear item',
    });
  }
};

const createGear = async (req, res) => {
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
      category_id,
      name,
      description,
      price_per_day,
      quantity,
      availability,
      condition_status,
      owner_name,
      image_url,
    } = req.body;

    const [result] = await pool.query(
      `INSERT INTO gear (
        category_id, name, description, price_per_day, quantity, availability,
        condition_status, owner_name, image_url
      ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`,
      [
        category_id,
        name,
        description || null,
        Number(price_per_day),
        Number(quantity || 0),
        availability || 'available',
        condition_status || null,
        owner_name || null,
        image_url || null,
      ]
    );

    return res.status(201).json({
      success: true,
      message: 'Gear item created successfully',
      data: {
        id: result.insertId,
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to create gear item',
    });
  }
};

const updateGear = async (req, res) => {
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
    const [existingRows] = await pool.query('SELECT * FROM gear WHERE id = ?', [req.params.id]);

    if (!existingRows.length) {
      return res.status(404).json({
        success: false,
        message: 'Gear item not found',
      });
    }

    const {
      category_id,
      name,
      description,
      price_per_day,
      quantity,
      availability,
      condition_status,
      owner_name,
      image_url,
    } = req.body;

    await pool.query(
      `UPDATE gear
       SET category_id = ?, name = ?, description = ?, price_per_day = ?, quantity = ?,
           availability = ?, condition_status = ?, owner_name = ?, image_url = ?
       WHERE id = ?`,
      [
        category_id ?? existingRows[0].category_id,
        name ?? existingRows[0].name,
        description ?? existingRows[0].description,
        Number(price_per_day ?? existingRows[0].price_per_day),
        Number(quantity ?? existingRows[0].quantity),
        availability ?? existingRows[0].availability,
        condition_status ?? existingRows[0].condition_status,
        owner_name ?? existingRows[0].owner_name,
        image_url ?? existingRows[0].image_url,
        req.params.id,
      ]
    );

    return res.status(200).json({
      success: true,
      message: 'Gear item updated successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to update gear item',
    });
  }
};

const deleteGear = async (req, res) => {
  try {
    const pool = getPool();
    const [result] = await pool.query('DELETE FROM gear WHERE id = ?', [req.params.id]);

    if (result.affectedRows === 0) {
      return res.status(404).json({
        success: false,
        message: 'Gear item not found',
      });
    }

    return res.status(200).json({
      success: true,
      message: 'Gear item deleted successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to delete gear item',
    });
  }
};

module.exports = {
  getGears,
  getGearById,
  createGear,
  updateGear,
  deleteGear,
};
