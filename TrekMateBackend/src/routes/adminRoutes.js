const express = require('express');
const { body } = require('express-validator');
const adminController = require('../controllers/adminController');
const { protect, authorize } = require('../middleware/authMiddleware');

const router = express.Router();

router.get('/logs', protect, authorize(3), adminController.getAdminLogs);

router.post(
  '/logs',
  protect,
  authorize(3),
  [
    body('action').trim().notEmpty().withMessage('Action is required'),
  ],
  adminController.logAdminAction
);

module.exports = router;
