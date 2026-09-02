const express = require('express');
const { body } = require('express-validator');
const reviewController = require('../controllers/reviewController');
const { protect } = require('../middleware/authMiddleware');

const router = express.Router();

router.get('/', reviewController.getReviews);
router.get('/:id', reviewController.getReviewById);

router.post(
  '/',
  protect,
  [
    body('target_type').isIn(['guide', 'package', 'gear', 'trek']).withMessage('Invalid target type'),
    body('target_id').isNumeric().withMessage('Target ID must be numeric'),
    body('rating').isInt({ min: 1, max: 5 }).withMessage('Rating must be between 1 and 5'),
  ],
  reviewController.createReview
);

router.put(
  '/:id',
  protect,
  [
    body('rating').optional().isInt({ min: 1, max: 5 }).withMessage('Rating must be between 1 and 5'),
  ],
  reviewController.updateReview
);

router.delete('/:id', protect, reviewController.deleteReview);

module.exports = router;
