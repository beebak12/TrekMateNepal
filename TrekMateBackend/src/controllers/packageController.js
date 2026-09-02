const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const getPackages = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM guide_packages ORDER BY created_at DESC');

    return res.status(200).json({
      success: true,
      data: rows,
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch guide packages',
    });
  }
};

const getPackageById = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM guide_packages WHERE id = ?', [req.params.id]);

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Package not found',
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
      message: 'Failed to fetch package details',
    });
  }
};

const createPackage = async (req, res) => {
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
      guide_id,
      trek_id,
      name,
      duration_days,
      price,
      description,
      included_services,
      excluded_services,
      max_people,
      itinerary,
      image_url,
      availability,
    } = req.body;

    const [result] = await pool.query(
      `INSERT INTO guide_packages (
        guide_id, trek_id, name, duration_days, price, description,
        included_services, excluded_services, max_people, itinerary, image_url, availability
      ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`,
      [
        guide_id,
        trek_id || null,
        name,
        Number(duration_days),
        Number(price),
        description || null,
        included_services || null,
        excluded_services || null,
        Number(max_people || 1),
        itinerary || null,
        image_url || null,
        availability || 'available',
      ]
    );

    return res.status(201).json({
      success: true,
      message: 'Guide package created successfully',
      data: { id: result.insertId },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to create guide package',
    });
  }
};

const updatePackage = async (req, res) => {
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
    const [rows] = await pool.query('SELECT * FROM guide_packages WHERE id = ?', [req.params.id]);

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Package not found',
      });
    }

    const {
      guide_id,
      trek_id,
      name,
      duration_days,
      price,
      description,
      included_services,
      excluded_services,
      max_people,
      itinerary,
      image_url,
      availability,
    } = req.body;

    await pool.query(
      `UPDATE guide_packages
       SET guide_id = ?, trek_id = ?, name = ?, duration_days = ?, price = ?, description = ?,
           included_services = ?, excluded_services = ?, max_people = ?, itinerary = ?,
           image_url = ?, availability = ?
       WHERE id = ?`,
      [
        guide_id ?? rows[0].guide_id,
        trek_id ?? rows[0].trek_id,
        name ?? rows[0].name,
        Number(duration_days ?? rows[0].duration_days),
        Number(price ?? rows[0].price),
        description ?? rows[0].description,
        included_services ?? rows[0].included_services,
        excluded_services ?? rows[0].excluded_services,
        Number(max_people ?? rows[0].max_people),
        itinerary ?? rows[0].itinerary,
        image_url ?? rows[0].image_url,
        availability ?? rows[0].availability,
        req.params.id,
      ]
    );

    return res.status(200).json({
      success: true,
      message: 'Guide package updated successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to update guide package',
    });
  }
};

const deletePackage = async (req, res) => {
  try {
    const pool = getPool();
    const [result] = await pool.query('DELETE FROM guide_packages WHERE id = ?', [req.params.id]);

    if (result.affectedRows === 0) {
      return res.status(404).json({
        success: false,
        message: 'Package not found',
      });
    }

    return res.status(200).json({
      success: true,
      message: 'Guide package deleted successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to delete guide package',
    });
  }
};

module.exports = {
  getPackages,
  getPackageById,
  createPackage,
  updatePackage,
  deletePackage,
};
