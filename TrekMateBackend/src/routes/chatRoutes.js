const express = require('express');
const { body } = require('express-validator');
const chatController = require('../controllers/chatController');
const { protect } = require('../middleware/authMiddleware');

const router = express.Router();

router.get('/conversations', protect, chatController.getConversations);
router.get('/conversations/:id/messages', protect, chatController.getConversationMessages);

router.post(
  '/conversations',
  protect,
  [
    body('user_two_id').isNumeric().withMessage('Receiver user ID must be numeric'),
  ],
  chatController.createConversation
);

router.post(
  '/messages',
  protect,
  [
    body('conversation_id').isNumeric().withMessage('Conversation ID must be numeric'),
    body('receiver_id').isNumeric().withMessage('Receiver ID must be numeric'),
    body('message_text').trim().notEmpty().withMessage('Message text is required'),
  ],
  chatController.sendMessage
);

module.exports = router;
