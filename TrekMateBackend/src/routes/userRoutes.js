const express = require('express');
const { body } = require('express-validator');
const userController = require('../controllers/userController');
const { protect } = require('../middleware/authMiddleware');
const { uploadProfileImage } = require('../middleware/uploadMiddleware');

const router = express.Router();

router.use(protect);

router.get('/profile', userController.getProfile);

router.put(
  '/profile',
  [
    body('full_name').optional().trim().notEmpty().withMessage('Full name cannot be empty'),
    body('username').optional().trim().isLength({ min: 3 }).withMessage('Username must be at least 3 characters'),
    body('email').optional().isEmail().withMessage('Please enter a valid email'),
    body('phone').optional().isMobilePhone().withMessage('Please enter a valid phone number'),
  ],
  userController.updateProfile
);

router.put(
  '/change-password',
  [
    body('current_password').notEmpty().withMessage('Current password is required'),
    body('new_password').isLength({ min: 6 }).withMessage('New password must be at least 6 characters'),
  ],
  userController.changePassword
);

router.post('/profile-image', uploadProfileImage.single('image'), userController.uploadProfileImageController);
router.delete('/account', userController.deleteAccount);

module.exports = router;
