const express = require('express');
const { body } = require('express-validator');
const notificationController = require('../controllers/notificationController');
const { protect, authorize } = require('../middleware/authMiddleware');

const router = express.Router();

router.get('/', protect, notificationController.getNotifications);
router.patch('/:id/read', protect, notificationController.markNotificationAsRead);

router.post(
  '/',
  protect,
  authorize(3),
  [
    body('user_id').isNumeric().withMessage('User ID must be numeric'),
    body('title').trim().notEmpty().withMessage('Title is required'),
    body('message').trim().notEmpty().withMessage('Message is required'),
  ],
  notificationController.createNotification
);

module.exports = router;
