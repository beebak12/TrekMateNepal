const express = require('express');
const { body } = require('express-validator');
const trekController = require('../controllers/trekController');
const { protect, authorize } = require('../middleware/authMiddleware');

const router = express.Router();

router.get('/', trekController.getTreks);
router.get('/:id', trekController.getTrekById);

router.post(
  '/',
  protect,
  authorize(3),
  [
    body('name').trim().notEmpty().withMessage('Trek name is required'),
    body('location').trim().notEmpty().withMessage('Location is required'),
    body('duration_days').isNumeric().withMessage('Duration must be numeric'),
    body('difficulty').optional().isIn(['Easy', 'Moderate', 'Difficult', 'Extreme']),
  ],
  trekController.createTrek
);

router.put(
  '/:id',
  protect,
  authorize(3),
  [
    body('name').optional().trim().notEmpty().withMessage('Trek name cannot be empty'),
    body('location').optional().trim().notEmpty().withMessage('Location cannot be empty'),
  ],
  trekController.updateTrek
);

router.delete('/:id', protect, authorize(3), trekController.deleteTrek);

module.exports = router;
