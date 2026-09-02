const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const toNumberOrNull = (value) => {
  if (value === undefined || value === null || value === '') return null;
  return Number(value);
};

const getTreks = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query('SELECT * FROM treks ORDER BY created_at DESC');

    return res.status(200).json({
      success: true,
      data: rows,
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch treks',
    });
  }
};

const getTrekById = async (req, res) => {
  try {
    const pool = getPool();
    const [trekRows] = await pool.query('SELECT * FROM treks WHERE id = ?', [req.params.id]);

    if (!trekRows.length) {
      return res.status(404).json({
        success: false,
        message: 'Trek not found',
      });
    }

    const [itineraryRows] = await pool.query(
      'SELECT * FROM trek_itineraries WHERE trek_id = ? ORDER BY day_number ASC',
      [req.params.id]
    );

    const [requirementRows] = await pool.query(
      'SELECT * FROM trek_requirements WHERE trek_id = ? ORDER BY id ASC',
      [req.params.id]
    );

    return res.status(200).json({
      success: true,
      data: {
        ...trekRows[0],
        itinerary: itineraryRows,
        requirements: requirementRows,
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch trek details',
    });
  }
};

const createTrek = async (req, res) => {
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
      name,
      location,
      duration_days,
      difficulty,
      max_altitude,
      price_min,
      price_max,
      description,
      best_season,
      starting_point,
      ending_point,
      required_permits,
      required_equipment,
      safety_info,
      image_url,
      itinerary,
      requirements,
    } = req.body;

    const [result] = await pool.query(
      `INSERT INTO treks (
        name, location, duration_days, difficulty, max_altitude, price_min, price_max,
        description, best_season, starting_point, ending_point, required_permits,
        required_equipment, safety_info, image_url
      ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`,
      [
        name,
        location,
        toNumberOrNull(duration_days),
        difficulty || 'Moderate',
        toNumberOrNull(max_altitude),
        toNumberOrNull(price_min),
        toNumberOrNull(price_max),
        description || null,
        best_season || null,
        starting_point || null,
        ending_point || null,
        required_permits || null,
        required_equipment || null,
        safety_info || null,
        image_url || null,
      ]
    );

    const trekId = result.insertId;

    if (Array.isArray(itinerary)) {
      for (const item of itinerary) {
        await pool.query(
          'INSERT INTO trek_itineraries (trek_id, day_number, title, description) VALUES (?, ?, ?, ?)',
          [trekId, Number(item.day_number), item.title, item.description || null]
        );
      }
    }

    if (Array.isArray(requirements)) {
      for (const item of requirements) {
        await pool.query(
          'INSERT INTO trek_requirements (trek_id, requirement_type, description) VALUES (?, ?, ?)',
          [trekId, item.requirement_type || 'equipment', item.description || '']
        );
      }
    }

    return res.status(201).json({
      success: true,
      message: 'Trek created successfully',
      data: { id: trekId },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to create trek',
    });
  }
};

const updateTrek = async (req, res) => {
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
    const [existingRows] = await pool.query('SELECT id FROM treks WHERE id = ?', [req.params.id]);

    if (!existingRows.length) {
      return res.status(404).json({
        success: false,
        message: 'Trek not found',
      });
    }

    const {
      name,
      location,
      duration_days,
      difficulty,
      max_altitude,
      price_min,
      price_max,
      description,
      best_season,
      starting_point,
      ending_point,
      required_permits,
      required_equipment,
      safety_info,
      image_url,
      itinerary,
      requirements,
    } = req.body;

    await pool.query(
      `UPDATE treks
       SET name = ?, location = ?, duration_days = ?, difficulty = ?, max_altitude = ?,
           price_min = ?, price_max = ?, description = ?, best_season = ?,
           starting_point = ?, ending_point = ?, required_permits = ?, required_equipment = ?,
           safety_info = ?, image_url = ?
       WHERE id = ?`,
      [
        name || existingRows[0].name,
        location || existingRows[0].location,
        toNumberOrNull(duration_days) || existingRows[0].duration_days,
        difficulty || existingRows[0].difficulty,
        toNumberOrNull(max_altitude) ?? existingRows[0].max_altitude,
        toNumberOrNull(price_min) ?? existingRows[0].price_min,
        toNumberOrNull(price_max) ?? existingRows[0].price_max,
        description ?? existingRows[0].description,
        best_season ?? existingRows[0].best_season,
        starting_point ?? existingRows[0].starting_point,
        ending_point ?? existingRows[0].ending_point,
        required_permits ?? existingRows[0].required_permits,
        required_equipment ?? existingRows[0].required_equipment,
        safety_info ?? existingRows[0].safety_info,
        image_url ?? existingRows[0].image_url,
        req.params.id,
      ]
    );

    if (Array.isArray(itinerary)) {
      await pool.query('DELETE FROM trek_itineraries WHERE trek_id = ?', [req.params.id]);
      for (const item of itinerary) {
        await pool.query(
          'INSERT INTO trek_itineraries (trek_id, day_number, title, description) VALUES (?, ?, ?, ?)',
          [req.params.id, Number(item.day_number), item.title, item.description || null]
        );
      }
    }

    if (Array.isArray(requirements)) {
      await pool.query('DELETE FROM trek_requirements WHERE trek_id = ?', [req.params.id]);
      for (const item of requirements) {
        await pool.query(
          'INSERT INTO trek_requirements (trek_id, requirement_type, description) VALUES (?, ?, ?)',
          [req.params.id, item.requirement_type || 'equipment', item.description || '']
        );
      }
    }

    return res.status(200).json({
      success: true,
      message: 'Trek updated successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to update trek',
    });
  }
};

const deleteTrek = async (req, res) => {
  try {
    const pool = getPool();
    const [result] = await pool.query('DELETE FROM treks WHERE id = ?', [req.params.id]);

    if (result.affectedRows === 0) {
      return res.status(404).json({
        success: false,
        message: 'Trek not found',
      });
    }

    return res.status(200).json({
      success: true,
      message: 'Trek deleted successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to delete trek',
    });
  }
};

module.exports = {
  getTreks,
  getTrekById,
  createTrek,
  updateTrek,
  deleteTrek,
};
