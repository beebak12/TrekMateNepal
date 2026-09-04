const express = require('express');
const { body } = require('express-validator');
const adminController = require('../controllers/adminController');
const finance = require('../controllers/adminFinanceController');
const users = require('../controllers/adminUserController');
const { protect, authorize } = require('../middleware/authMiddleware');

const router = express.Router();

router.use(protect, authorize(3));

router.get('/dashboard', finance.getDashboard);

router.get('/users', users.getUsers);
router.post('/users', [
  body('full_name').trim().notEmpty(),
  body('username').trim().notEmpty(),
  body('email').isEmail().normalizeEmail(),
  body('password').isLength({ min: 8 }),
  body('role_id').optional().isInt({ min: 1, max: 3 }),
  body('gender').optional({ nullable: true }).isIn(['MALE','FEMALE','OTHER','PREFER_NOT_TO_SAY']),
  body('dob').optional({ nullable: true }).isISO8601(),
], users.createUser);
router.put('/users/:id', [
  body('full_name').optional().trim().notEmpty(),
  body('username').optional().trim().notEmpty(),
  body('email').optional().isEmail().normalizeEmail(),
  body('role_id').optional().isInt({ min: 1, max: 3 }),
  body('gender').optional({ nullable: true }).isIn(['MALE','FEMALE','OTHER','PREFER_NOT_TO_SAY']),
  body('dob').optional({ nullable: true }).isISO8601(),
], users.updateUser);
router.patch('/users/:id/status', body('is_active').isBoolean(), users.setUserStatus);
router.get('/providers', users.getProviders);
router.get('/providers/:id/history', users.getProviderHistory);

router.get('/transactions', finance.getTransactions);
router.post('/transactions', [
  body('customer_id').isInt({ min: 1 }),
  body('provider_id').optional({ nullable: true }).isInt({ min: 1 }),
  body('gross_amount').isFloat({ gt: 0 }),
  body('gateway').optional().isIn(['ESEWA','KHALTI','MANUAL','SANDBOX']),
], finance.createTransaction);
router.patch('/transactions/:id/verify', [
  body('verified').isBoolean(),
  body('gateway_transaction_id').optional({ nullable: true }).isString().trim().notEmpty(),
], finance.verifyTransaction);

router.get('/payouts', finance.getPayouts);
router.patch('/payouts/:id', [
  body('status').isIn(['APPROVED','PAID','REJECTED']),
  body('payout_reference').optional({ nullable: true }).isString().trim().notEmpty(),
], finance.updatePayout);

router.get('/settlements', finance.getSettlements);
router.post('/settlements', [
  body('provider_id').isInt({ min: 1 }),
  body('period_start').isISO8601({ strict: true }),
  body('period_end').isISO8601({ strict: true }),
], finance.createSettlement);
router.patch('/settlements/:id', [
  body('status').isIn(['APPROVED','PAID','REJECTED']),
  body('payout_reference').optional({ nullable: true }).isString().trim().notEmpty(),
], finance.updateSettlement);

router.get('/reports/monthly', finance.getMonthlyReport);

router.get('/refunds', finance.getRefunds);
router.post('/refunds', [
  body('transaction_id').isInt({ min: 1 }),
  body('amount').isFloat({ gt: 0 }),
  body('reason').trim().notEmpty(),
], finance.createRefund);
router.patch('/refunds/:id', [
  body('status').isIn(['APPROVED','COMPLETED','REJECTED']),
  body('refund_reference').optional({ nullable: true }).isString().trim().notEmpty(),
], finance.updateRefund);

router.get('/logs', adminController.getAdminLogs);
router.post('/logs', body('action').trim().notEmpty(), adminController.logAdminAction);

module.exports = router;
