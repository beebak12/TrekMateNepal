const express = require('express');
const { body } = require('express-validator');
const guideController = require('../controllers/guideController');
const { protect, authorize } = require('../middleware/authMiddleware');

const router = express.Router();

router.get('/', guideController.getGuides);
router.get('/:id', guideController.getGuideById);

router.post(
  '/',
  protect,
  authorize(3),
  [
    body('full_name').trim().notEmpty().withMessage('Guide name is required'),
    body('license_number').trim().notEmpty().withMessage('License number is required'),
    body('experience_years').isNumeric().withMessage('Experience must be numeric'),
  ],
  guideController.createGuide
);

router.put(
  '/:id',
  protect,
  authorize(3),
  [
    body('full_name').optional().trim().notEmpty().withMessage('Guide name cannot be empty'),
    body('license_number').optional().trim().notEmpty().withMessage('License number cannot be empty'),
  ],
  guideController.updateGuide
);

router.delete('/:id', protect, authorize(3), guideController.deleteGuide);

module.exports = router;
