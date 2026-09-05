# TrekMate Nepal Admin API

All endpoints below are prefixed with `/api/admin` and require an administrator JWT:

```http
Authorization: Bearer <admin-jwt>
```

## Dashboard and users

| Method | Endpoint | Purpose |
| --- | --- | --- |
| GET | `/dashboard` | Revenue, payout, refund and verification totals |
| GET | `/users` | Search/filter users with gear and earnings totals |
| POST | `/users` | Add a user |
| PUT | `/users/:id` | Update a user |
| PATCH | `/users/:id/status` | Activate/deactivate a user without deleting history |
| GET | `/providers` | List users who own gear |
| GET | `/providers/:id/history` | Provider gear, booking and transaction history |

### User roles and provider status

The account roles stored by the current backend are `USER`, `GUIDE`, and
`ADMIN`. A gear provider is not a separate account role. A standard `USER`
becomes a provider by listing gear, and the same account may also rent gear
from another owner. Admin responses and the web panel therefore identify
providers from their gear ownership (`gear_listed > 0`) while authorization
continues to use the stored `USER` role.

## Payments

| Method | Endpoint | Purpose |
| --- | --- | --- |
| GET | `/transactions` | Search/filter financial transactions |
| POST | `/transactions` | Record a pending transaction and calculate the 10/90 split |
| PATCH | `/transactions/:id/verify` | Save a verified/failed result and create a pending payout |
| GET | `/payouts` | List provider payouts |
| PATCH | `/payouts/:id` | Approve, reject or mark a payout paid |
| GET | `/settlements` | List combined weekly provider settlements |
| POST | `/settlements` | Group a provider's pending payouts for a period of up to seven days |
| PATCH | `/settlements/:id` | Approve, reject or mark an entire weekly settlement paid |
| GET | `/reports/monthly?month=YYYY-MM` | Generate monthly revenue, refund and settlement totals |
| GET | `/refunds` | List refunds |
| POST | `/refunds` | Request a refund against a verified transaction |
| PATCH | `/refunds/:id` | Approve, reject or complete a refund |

## Important payment limitation

The verification endpoint stores a verification result supplied by trusted backend/admin logic. It does **not** contact eSewa or Khalti by itself. A real deployment must call the selected gateway's server-to-server lookup API, compare the amount and transaction identifier, and only then pass the verified result into this workflow.

The prototype records a 10/90 split for every verified transaction. Pending provider amounts can be grouped into a weekly settlement and marked paid with one manual transfer reference. It does not automatically transfer money to a provider account.

## Local setup

Copy `.env.example` to `.env`, set local values, then run:

```bash
npm install
npm start
```

On macOS, AirPlay may occupy port `5000`. A developer can use `PORT=5050` locally without committing that private `.env` change.
