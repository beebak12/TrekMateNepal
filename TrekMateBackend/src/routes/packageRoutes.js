const express = require('express');
const { body } = require('express-validator');
const packageController = require('../controllers/packageController');
const { protect, authorize } = require('../middleware/authMiddleware');

const router = express.Router();

router.get('/', packageController.getPackages);
router.get('/:id', packageController.getPackageById);

router.post(
  '/',
  protect,
  authorize(3),
  [
    body('guide_id').notEmpty().withMessage('Guide ID is required'),
    body('name').trim().notEmpty().withMessage('Package name is required'),
    body('duration_days').isNumeric().withMessage('Duration must be numeric'),
    body('price').isNumeric().withMessage('Price must be numeric'),
  ],
  packageController.createPackage
);

router.put(
  '/:id',
  protect,
  authorize(3),
  [
    body('name').optional().trim().notEmpty().withMessage('Package name cannot be empty'),
    body('price').optional().isNumeric().withMessage('Price must be numeric'),
  ],
  packageController.updatePackage
);

router.delete('/:id', protect, authorize(3), packageController.deletePackage);

module.exports = router;
