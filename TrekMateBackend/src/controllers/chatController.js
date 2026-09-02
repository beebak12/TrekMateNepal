const { validationResult } = require('express-validator');
const { getPool } = require('../config/db');

const getConversations = async (req, res) => {
  try {
    const pool = getPool();
    const [rows] = await pool.query(
      `SELECT * FROM conversations
       WHERE user_one_id = ? OR user_two_id = ?
       ORDER BY last_message_at DESC, created_at DESC`,
      [req.user.id, req.user.id]
    );

    return res.status(200).json({
      success: true,
      data: rows,
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch conversations',
    });
  }
};

const getConversationMessages = async (req, res) => {
  try {
    const pool = getPool();
    const [conversationRows] = await pool.query(
      `SELECT * FROM conversations
       WHERE id = ? AND (user_one_id = ? OR user_two_id = ?)`,
      [req.params.id, req.user.id, req.user.id]
    );

    if (!conversationRows.length) {
      return res.status(404).json({
        success: false,
        message: 'Conversation not found',
      });
    }

    const [rows] = await pool.query(
      `SELECT * FROM messages
       WHERE conversation_id = ?
       ORDER BY created_at ASC`,
      [req.params.id]
    );

    return res.status(200).json({
      success: true,
      data: rows,
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to fetch conversation messages',
    });
  }
};

const createConversation = async (req, res) => {
  const errors = validationResult(req);

  if (!errors.isEmpty()) {
    return res.status(400).json({
      success: false,
      message: 'Validation failed',
      errors: errors.array(),
    });
  }

  try {
    const pool = getPool();
    const { user_two_id } = req.body;

    if (Number(user_two_id) === Number(req.user.id)) {
      return res.status(400).json({
        success: false,
        message: 'You cannot create a conversation with yourself',
      });
    }

    const [existing] = await pool.query(
      `SELECT * FROM conversations
       WHERE (user_one_id = ? AND user_two_id = ?)
          OR (user_one_id = ? AND user_two_id = ?)`,
      [req.user.id, user_two_id, user_two_id, req.user.id]
    );

    if (existing.length) {
      return res.status(200).json({
        success: true,
        message: 'Conversation already exists',
        data: existing[0],
      });
    }

    const [result] = await pool.query(
      `INSERT INTO conversations (user_one_id, user_two_id)
       VALUES (?, ?)`,
      [req.user.id, user_two_id]
    );

    return res.status(201).json({
      success: true,
      message: 'Conversation created successfully',
      data: {
        id: result.insertId,
        user_one_id: req.user.id,
        user_two_id,
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to create conversation',
    });
  }
};

const sendMessage = async (req, res) => {
  const errors = validationResult(req);

  if (!errors.isEmpty()) {
    return res.status(400).json({
      success: false,
      message: 'Validation failed',
      errors: errors.array(),
    });
  }

  try {
    const pool = getPool();
    const { conversation_id, receiver_id, message_text, message_type } = req.body;

    const [conversationRows] = await pool.query(
      `SELECT * FROM conversations
       WHERE id = ? AND (user_one_id = ? OR user_two_id = ?)`,
      [conversation_id, req.user.id, req.user.id]
    );

    if (!conversationRows.length) {
      return res.status(404).json({
        success: false,
        message: 'Conversation not found',
      });
    }

    const receiver = Number(receiver_id);
    const sender = Number(req.user.id);

    if (receiver === sender) {
      return res.status(400).json({
        success: false,
        message: 'Receiver must be a different user',
      });
    }

    const [result] = await pool.query(
      `INSERT INTO messages (conversation_id, sender_id, receiver_id, message_text, message_type)
       VALUES (?, ?, ?, ?, ?)`,
      [conversation_id, sender, receiver, message_text, message_type || 'text']
    );

    await pool.query(
      'UPDATE conversations SET last_message_at = NOW() WHERE id = ?',
      [conversation_id]
    );

    return res.status(201).json({
      success: true,
      message: 'Message sent successfully',
      data: {
        id: result.insertId,
        conversation_id,
        sender_id: sender,
        receiver_id: receiver,
        message_text,
        message_type: message_type || 'text',
      },
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      success: false,
      message: 'Failed to send message',
    });
  }
};

module.exports = {
  getConversations,
  getConversationMessages,
  createConversation,
  sendMessage,
};
