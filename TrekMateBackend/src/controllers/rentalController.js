const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const getDayDifference = (startDate, endDate) => {
  const start = new Date(startDate);
  const end = new Date(endDate);
  const diffMs = end.getTime() - start.getTime();
  const diffDays = diffMs / (1000 * 60 * 60 * 24);
  return Math.max(0, diffDays + 1);
};

const getRentals = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query(
      `SELECT * FROM gear_rentals WHERE user_id = ? ORDER BY created_at DESC`,
      [req.user.id]
    );

    return res.status(200).json({
      success: true,
      data: rows,
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch rental bookings',
    });
  }
};

const getRentalById = async (req, res) => {
  try {
    const pool = getPool();
    const [rentalRows] = await pool.query(
      `SELECT * FROM gear_rentals WHERE id = ? AND user_id = ?`,
      [req.params.id, req.user.id]
    );

    if (!rentalRows.length) {
      return res.status(404).json({
        success: false,
        message: 'Rental booking not found',
      });
    }

    const [items] = await pool.query(
      `SELECT ri.*, g.name AS gear_name, g.price_per_day
       FROM rental_items ri
       JOIN gear g ON g.id = ri.gear_id
       WHERE ri.rental_id = ?`,
      [req.params.id]
    );

    return res.status(200).json({
      success: true,
      data: {
        ...rentalRows[0],
        items,
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch rental booking',
    });
  }
};

const createRental = async (req, res) => {
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
    const { start_date, end_date, items } = req.body;

    if (!Array.isArray(items) || items.length === 0) {
      return res.status(400).json({
        success: false,
        message: 'At least one rental item is required',
      });
    }

    if (new Date(end_date) < new Date(start_date)) {
      return res.status(400).json({
        success: false,
        message: 'Rental end date cannot be earlier than start date',
      });
    }

    const rentalDays = getDayDifference(start_date, end_date);
    let totalPrice = 0;
    const validatedItems = [];

    for (const item of items) {
      const [gearRows] = await pool.query('SELECT * FROM gear WHERE id = ?', [item.gear_id]);

      if (!gearRows.length) {
        return res.status(404).json({
          success: false,
          message: `Gear item ${item.gear_id} not found`,
        });
      }

      const gear = gearRows[0];
      const quantity = Number(item.quantity || 0);

      if (quantity <= 0) {
        return res.status(400).json({
          success: false,
          message: 'Item quantity must be greater than zero',
        });
      }

      if (gear.quantity < quantity) {
        return res.status(400).json({
          success: false,
          message: `Not enough stock for ${gear.name}`,
        });
      }

      const subtotal = quantity * rentalDays * Number(gear.price_per_day);
      totalPrice += subtotal;

      validatedItems.push({
        gear_id: gear.id,
        quantity,
        unit_price: Number(gear.price_per_day),
        subtotal,
      });
    }

    const [rentalResult] = await pool.query(
      `INSERT INTO gear_rentals (user_id, start_date, end_date, total_price, status)
       VALUES (?, ?, ?, ?, 'pending')`,
      [req.user.id, start_date, end_date, totalPrice.toFixed(2)]
    );

    const rentalId = rentalResult.insertId;

    for (const item of validatedItems) {
      await pool.query(
        `INSERT INTO rental_items (rental_id, gear_id, quantity, unit_price, subtotal)
         VALUES (?, ?, ?, ?, ?)`,
        [rentalId, item.gear_id, item.quantity, item.unit_price, item.subtotal]
      );

      await pool.query(
        'UPDATE gear SET quantity = quantity - ? WHERE id = ?',
        [item.quantity, item.gear_id]
      );
    }

    return res.status(201).json({
      success: true,
      message: 'Rental booking created successfully',
      data: {
        id: rentalId,
        user_id: req.user.id,
        start_date,
        end_date,
        total_price: Number(totalPrice.toFixed(2)),
        status: 'pending',
        items: validatedItems,
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to create rental booking',
    });
  }
};

const cancelRental = async (req, res) => {
  try {
    const pool = getPool();
    const [rentalRows] = await pool.query(
      'SELECT * FROM gear_rentals WHERE id = ? AND user_id = ?',
      [req.params.id, req.user.id]
    );

    if (!rentalRows.length) {
      return res.status(404).json({
        success: false,
        message: 'Rental booking not found',
      });
    }

    const rental = rentalRows[0];
    if (rental.status === 'cancelled') {
      return res.status(400).json({
        success: false,
        message: 'Rental booking is already cancelled',
      });
    }

    await pool.query(
      'UPDATE gear_rentals SET status = ? WHERE id = ?',
      ['cancelled', req.params.id]
    );

    const [items] = await pool.query('SELECT * FROM rental_items WHERE rental_id = ?', [req.params.id]);
    for (const item of items) {
      await pool.query('UPDATE gear SET quantity = quantity + ? WHERE id = ?', [item.quantity, item.gear_id]);
    }

    return res.status(200).json({
      success: true,
      message: 'Rental booking cancelled successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to cancel rental booking',
    });
  }
};

module.exports = {
  getRentals,
  getRentalById,
  createRental,
  cancelRental,
};
