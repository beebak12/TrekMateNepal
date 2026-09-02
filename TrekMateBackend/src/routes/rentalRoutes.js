const express = require('express');
const { body } = require('express-validator');
const rentalController = require('../controllers/rentalController');
const { protect } = require('../middleware/authMiddleware');

const router = express.Router();

router.use(protect);

router.get('/', rentalController.getRentals);
router.get('/:id', rentalController.getRentalById);

router.post(
  '/',
  [
    body('start_date').notEmpty().withMessage('Start date is required'),
    body('end_date').notEmpty().withMessage('End date is required'),
    body('items').isArray({ min: 1 }).withMessage('At least one rental item is required'),
    body('items.*.gear_id').notEmpty().withMessage('Gear ID is required'),
    body('items.*.quantity').isNumeric().withMessage('Quantity must be numeric'),
  ],
  rentalController.createRental
);

router.put('/:id/cancel', rentalController.cancelRental);

module.exports = router;
