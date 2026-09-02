const express = require('express');
const { body } = require('express-validator');
const favoriteController = require('../controllers/favoriteController');
const { protect } = require('../middleware/authMiddleware');

const router = express.Router();

router.get('/', protect, favoriteController.getFavorites);

router.post(
  '/',
  protect,
  [
    body('entity_type').isIn(['trek', 'gear', 'guide', 'package']).withMessage('Invalid entity type'),
    body('entity_id').isNumeric().withMessage('Entity ID must be numeric'),
  ],
  favoriteController.addFavorite
);

router.delete('/:entity_type/:entity_id', protect, favoriteController.removeFavorite);

module.exports = router;
