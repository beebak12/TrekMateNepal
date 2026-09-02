const express = require('express');
const { body } = require('express-validator');
const postController = require('../controllers/postController');
const { protect } = require('../middleware/authMiddleware');

const router = express.Router();

router.get('/', postController.getPosts);
router.get('/:id', postController.getPostById);

router.post(
  '/',
  protect,
  [
    body('content').notEmpty().withMessage('Post content is required'),
  ],
  postController.createPost
);

router.put(
  '/:id',
  protect,
  [
    body('content').optional().notEmpty().withMessage('Content cannot be empty'),
  ],
  postController.updatePost
);

router.delete('/:id', protect, postController.deletePost);

module.exports = router;
