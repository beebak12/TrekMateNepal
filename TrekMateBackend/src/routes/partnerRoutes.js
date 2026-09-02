const express = require('express');
const { body } = require('express-validator');
const partnerController = require('../controllers/partnerController');
const { protect } = require('../middleware/authMiddleware');

const router = express.Router();

router.get('/', partnerController.getPartnerPosts);
router.get('/:id', partnerController.getPartnerPostById);

router.post(
  '/',
  protect,
  [
    body('required_partners').isNumeric().withMessage('Required partners must be numeric'),
    body('travel_date').notEmpty().withMessage('Travel date is required'),
  ],
  partnerController.createPartnerPost
);

router.put(
  '/:id',
  protect,
  [
    body('required_partners').optional().isNumeric().withMessage('Required partners must be numeric'),
    body('travel_date').optional().notEmpty().withMessage('Travel date cannot be empty'),
  ],
  partnerController.updatePartnerPost
);

router.delete('/:id', protect, partnerController.deletePartnerPost);
router.post('/:id/request', protect, partnerController.requestPartner);

module.exports = router;
