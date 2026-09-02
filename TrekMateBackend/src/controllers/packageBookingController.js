const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const getPackageBookings = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query(
      'SELECT * FROM package_bookings WHERE user_id = ? ORDER BY created_at DESC',
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
      message: 'Failed to fetch package bookings',
    });
  }
};

const getPackageBookingById = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query(
      'SELECT * FROM package_bookings WHERE id = ? AND user_id = ?',
      [req.params.id, req.user.id]
    );

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Package booking not found',
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
      message: 'Failed to fetch package booking',
    });
  }
};

const createPackageBooking = async (req, res) => {
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
    const { package_id, booking_date, number_of_people } = req.body;

    const [packageRows] = await pool.query('SELECT * FROM guide_packages WHERE id = ?', [package_id]);
    if (!packageRows.length) {
      return res.status(404).json({
        success: false,
        message: 'Package not found',
      });
    }

    const pkg = packageRows[0];
    const numberPeople = Number(number_of_people || 1);
    const totalPrice = Number(pkg.price) * numberPeople;

    const [result] = await pool.query(
      `INSERT INTO package_bookings (user_id, package_id, booking_date, number_of_people, total_price, status)
       VALUES (?, ?, ?, ?, ?, 'pending')`,
      [req.user.id, package_id, booking_date, numberPeople, totalPrice.toFixed(2)]
    );

    return res.status(201).json({
      success: true,
      message: 'Guide package booking created successfully',
      data: {
        id: result.insertId,
        user_id: req.user.id,
        package_id,
        booking_date,
        number_of_people: numberPeople,
        total_price: Number(totalPrice.toFixed(2)),
        status: 'pending',
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to create package booking',
    });
  }
};

const cancelPackageBooking = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query(
      'SELECT * FROM package_bookings WHERE id = ? AND user_id = ?',
      [req.params.id, req.user.id]
    );

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Package booking not found',
      });
    }

    if (rows[0].status === 'cancelled') {
      return res.status(400).json({
        success: false,
        message: 'Package booking is already cancelled',
      });
    }

    await pool.query(
      'UPDATE package_bookings SET status = ? WHERE id = ?',
      ['cancelled', req.params.id]
    );

    return res.status(200).json({
      success: true,
      message: 'Package booking cancelled successfully',
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to cancel package booking',
    });
  }
};

module.exports = {
  getPackageBookings,
  getPackageBookingById,
  createPackageBooking,
  cancelPackageBooking,
};
