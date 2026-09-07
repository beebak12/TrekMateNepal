# TrekMate Nepal admin panel

This is the selected admin/payment web frontend, not Android Java code.

## Location and startup

- Android stays in `D:\AndroidProjects\TrekMateNepal\app`.
- Backend stays in `D:\AndroidProjects\TrekMateBackend` (the existing backend, not a nested duplicate).
- Admin HTML, CSS and browser JavaScript live in this `admin panel` folder.

Start the existing backend using its normal `npm start`, then open
`http://localhost:5000/admin/`. If the backend uses another port, use that port.
The panel calls `/api` on the same origin. Do not open the HTML as a `file://` page.
For a different deployment folder, set `ADMIN_PANEL_DIR` in the backend environment.
Only this public frontend folder is served; no backend source or `.env` is exposed.

Use an existing active account with database role `ADMIN` (`role_id = 3`).
No default admin credentials, automatic promotions or Android login bypasses are added.
An administrator account must be provisioned separately if none exists.

## Selective integration

Source: `origin/feature/admin-integration` at `1c68b7b`. It already includes
`feature/admin-backend` (`2f8a134`) and `feature/payment-admin-panel` (`d6cbabd`).
Only the admin frontend, admin APIs, payment schema, and required gear-owner and
account-deactivation integration were selected. Existing backend dependencies,
database credentials, authentication contracts, profiles, chat and Android files
were preserved. No full-branch merge, commit or push was performed.

## Payment scope

This is a manual/sandbox administration workflow. Verification records an admin's
decision; it does not call eSewa or Khalti. Marking transfers/refunds completed
records an external transfer reference; it does not move money.
There is no newly added Android checkout/gateway integration.

Refund reservations cannot exceed the payment amount. Completing an unbatched,
pending payout's refund reduces its payable using the existing 10/90 split.
Refunds affecting approved, paid or batched payouts require manual provider
reconciliation; completion is blocked instead of silently changing transfer history.
Monthly reports show gross transaction splits and refunds separately, not a net
accounting ledger. Existing gear owner names are not automatically matched to user
accounts; provider reporting requires an explicit `owner_user_id` link.

## Verification

Backend regression tests: from the backend folder, run
`node --test test/admin-integration.test.js`.
These create and remove an isolated `trekmate_admin_test_*` MySQL database using
the local connection settings; they never modify the application database.

Frontend checks: from this folder run `node --test tests/panel.test.js`.

The existing backend is outside the Android Git repository. Include its selected
changes in your backend's own version-control/deployment workflow; committing this
Android repository alone will not include those backend files.

### Checked on 6 September 2026

- Android `assembleDebug`: successful.
- Backend MySQL/HTTP suite: 8 passed, 0 failed (includes the parent suite).
- Frontend script/DOM-contract/session checks: 4 passed, 0 failed.
- All 44 backend JavaScript source files passed syntax checks.
- All 499 tracked Android app files matched their pre-integration hashes, including
  the three pre-existing layout edits. No Android Java or resources were removed.
- Temporary test database was cleaned up. The application database was left at
  its original 25 tables with no account promotions or test records.
- Backend HTTP service was not running at handoff. Its next normal startup will
  apply the four payment tables and nullable gear-owner column, then serve `/admin/`.
- No active ADMIN account exists in the application database yet; account
  provisioning needs an explicit account choice. Real gateway processing and
  provider reconciliation remain outside this prototype integration.

Pre-integration copies of the modified existing backend files are available at
`C:\Users\paude\AppData\Local\Temp\trekmate-admin-merge-backup-20260906`.
These temporary backups may be cleared by Windows; keep a durable copy if needed.
