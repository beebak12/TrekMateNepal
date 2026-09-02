const express = require('express');
const { body } = require('express-validator');
const emergencyController = require('../controllers/emergencyController');
const { protect, authorize } = require('../middleware/authMiddleware');

const router = express.Router();

router.get('/', emergencyController.getEmergencyContacts);
router.get('/:id', emergencyController.getEmergencyContactById);

router.post(
  '/',
  protect,
  authorize(3),
  [
    body('name').trim().notEmpty().withMessage('Name is required'),
    body('type').isIn(['police', 'tourist_police', 'hospital', 'rescue', 'helipad', 'general']).withMessage('Invalid emergency type'),
    body('phone').trim().notEmpty().withMessage('Phone is required'),
  ],
  emergencyController.createEmergencyContact
);

router.put(
  '/:id',
  protect,
  authorize(3),
  [
    body('name').optional().trim().notEmpty().withMessage('Name cannot be empty'),
    body('phone').optional().trim().notEmpty().withMessage('Phone cannot be empty'),
  ],
  emergencyController.updateEmergencyContact
);

router.delete('/:id', protect, authorize(3), emergencyController.deleteEmergencyContact);

module.exports = router;
