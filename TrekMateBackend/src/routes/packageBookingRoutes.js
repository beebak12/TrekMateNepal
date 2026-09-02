const express = require('express');
const { body } = require('express-validator');
const packageBookingController = require('../controllers/packageBookingController');
const { protect } = require('../middleware/authMiddleware');

const router = express.Router();

router.get('/', protect, packageBookingController.getPackageBookings);
router.get('/:id', protect, packageBookingController.getPackageBookingById);

router.post(
  '/',
  protect,
  [
    body('package_id').notEmpty().withMessage('Package ID is required'),
    body('booking_date').notEmpty().withMessage('Booking date is required'),
    body('number_of_people').isInt({ min: 1 }).withMessage('Number of people must be at least 1'),
  ],
  packageBookingController.createPackageBooking
);

router.patch('/:id/cancel', protect, packageBookingController.cancelPackageBooking);

module.exports = router;
