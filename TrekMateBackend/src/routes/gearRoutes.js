const express = require('express');
const { body } = require('express-validator');
const gearController = require('../controllers/gearController');
const { protect, authorize } = require('../middleware/authMiddleware');

const router = express.Router();

router.get('/', gearController.getGears);
router.get('/:id', gearController.getGearById);

router.post(
  '/',
  protect,
  authorize(3),
  [
    body('category_id').notEmpty().withMessage('Category is required'),
    body('name').trim().notEmpty().withMessage('Gear name is required'),
    body('price_per_day').isNumeric().withMessage('Price per day must be numeric'),
    body('quantity').isNumeric().withMessage('Quantity must be numeric'),
  ],
  gearController.createGear
);

router.put(
  '/:id',
  protect,
  authorize(3),
  [
    body('name').optional().trim().notEmpty().withMessage('Gear name cannot be empty'),
    body('price_per_day').optional().isNumeric().withMessage('Price per day must be numeric'),
  ],
  gearController.updateGear
);

router.delete('/:id', protect, authorize(3), gearController.deleteGear);

module.exports = router;
