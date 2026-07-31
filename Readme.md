# 📚 BookSeva

A trusted, student-only marketplace where verified college students can buy, sell, or donate used textbooks at fair, condition-based prices — with built-in fraud protection, dispute resolution, and admin moderation.

> New textbooks are expensive and used for one semester. Students have no easy, *trustworthy* way to resell them — WhatsApp groups and notice boards have no identity verification, no fair pricing logic, and no protection against scams. BookSeva solves this with email-verified accounts, condition-driven pricing, and an admin-backed trust system.

---

## ✨ Features

- 🔐 **Verified accounts only** — email OTP verification gates every account before they can buy or sell
- 💰 **Condition-based pricing** — sellers pick a condition tier (Excellent / Good / Fair / Poor / Donate), and the system suggests a fair price band based on MRP
- 🔍 **Smart search & discovery** — filter by category, condition, price range, college; sort and paginate results
- 🛒 **Cart & checkout** — add multiple books, checkout per-seller, pay securely via Razorpay
- 📦 **Full order lifecycle** — placed → paid → confirmed by seller → handed over → buyer confirms receipt → completed, with a 48-hour confirmation window and auto-complete
- 🧾 **Automatic invoicing** — a PDF invoice is generated and downloadable once an order completes
- ⚠️ **Dispute resolution** — buyers can flag a condition mismatch within the confirmation window; admins resolve with full/partial refund or dismissal, and it affects the seller's trust score
- 🚩 **Reporting & moderation** — flag suspicious listings or users; admins review and act from a dedicated queue
- ⭐ **Reviews** — two-way ratings after a completed order, feeding into each user's trust score
- 💬 **In-app messaging** — buyer↔seller chat scoped to a specific listing, with a dedicated inbox and unread badges
- 🔔 **Notifications** — in-app + email for OTP, order events, dispute updates, and report resolutions
- 🛠️ **Admin dashboard** — manage users, listings, reports, disputes, and platform settings from one place

---

## 🧱 Tech Stack

**Backend**
- Java 17 · Spring Boot 4
- Spring Security + JWT (access + refresh tokens)
- Spring Data JPA / Hibernate
- MySQL
- Maven

**Frontend**
- React 19 (Vite)
- Tailwind CSS 4
- TanStack Query
- React Router v7
- React Hook Form + Zod

**Integrations**
- Razorpay (payments)
- Cloudinary (image storage)
- Spring Mail / SMTP (transactional email)

---

## 🏗️ Architecture

A modular monolith — package-by-feature on the backend, so each domain owns its own controller/service/repository/entity, while still deploying as a single Spring Boot application.

```
backend/bookseva/
 ├── auth/            registration, login, OTP, JWT, refresh tokens
 ├── user/            profile management
 ├── book/            listings, condition tiers, suggested pricing
 ├── search/          search, filters, sort, pagination
 ├── cart/            cart items
 ├── order/           checkout, order lifecycle, auto-confirm job
 ├── payment/         Razorpay integration (webhook + verify)
 ├── invoice/         PDF invoice generation & retrieval
 ├── notification/    in-app notifications + email dispatch
 ├── review/          ratings
 ├── message/         in-app chat
 ├── report/          listing/user reporting
 ├── dispute/         condition-mismatch disputes & resolution
 ├── admin/           dashboard, moderation, platform settings
 ├── common/          shared exception handling, DTO base classes
 └── config/          security, CORS, JWT filter, admin seeding

frontend/src/
 ├── pages/           Home, BookDetails, CreateListing, EditListing,
 │                    Cart, Profile (orders/sales/listings/edit),
 │                    Login, Register, VerifyOTP, ForgotPassword,
 │                    Inbox, AdminDashboard
 ├── components/      Navbar, ChatBox, ReportModal, ReviewModal,
 │                    DisputeModal, AdminRoute
 └── context/         AuthContext
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven
- Node.js + npm
- MySQL (running locally)

### 1. Database

```sql
CREATE DATABASE bookseva;
```

### 2. Backend

```bash
cd backend
cp .env.example .env   # fill in your values — see Configuration below
mvn clean install
mvn spring-boot:run
```

The API runs on `http://localhost:8080`.

### 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

The app runs on `http://localhost:5173`.

> Start the backend first and wait for it to fully come up before starting the frontend, or the first API calls will fail with connection-refused errors.

---

## ⚙️ Configuration

### Backend environment variables

```env
# Database
DB_URL=jdbc:mysql://localhost:3306/bookseva
DB_USERNAME=
DB_PASSWORD=

# JWT
JWT_SECRET=
JWT_ACCESS_EXPIRY=900000
JWT_REFRESH_EXPIRY=604800000

# Mail
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=
MAIL_PASSWORD=

# Cloudinary
CLOUDINARY_CLOUD_NAME=
CLOUDINARY_API_KEY=
CLOUDINARY_API_SECRET=

# Razorpay
RAZORPAY_KEY_ID=
RAZORPAY_KEY_SECRET=

# App config
ALLOWED_EMAIL_DOMAINS=
FRONTEND_ORIGIN=http://localhost:5173
PLATFORM_FEE_PERCENT=0
ADMIN_SEED_EMAIL=
ADMIN_SEED_PASSWORD=
```

### Frontend environment variables

```env
VITE_API_BASE_URL=http://localhost:8080/api/v1
VITE_RAZORPAY_KEY_ID=
VITE_CLOUDINARY_CLOUD_NAME=
```

> **Never commit real credentials.** Rotate any secret that's ever been hardcoded or shared.

---

## 📡 API Overview

All endpoints are prefixed with `/api/v1`.

| Area | Examples |
|---|---|
| Auth | `POST /auth/register`, `/auth/verify-otp`, `/auth/login`, `/auth/refresh`, `/auth/forgot-password`, `/auth/reset-password` |
| Books | `GET /books`, `POST /books`, `PUT /books/{id}`, `GET /books/me`, `GET /books/suggested-price` |
| Search | `GET /search/books` |
| Cart | `GET /cart`, `POST /cart/items`, `DELETE /cart/items/{id}` |
| Orders | `POST /orders/checkout`, `GET /orders/mine`, `PATCH /orders/{id}/status`, `POST /orders/{id}/cancel` |
| Payments | `POST /payments/webhook`, `POST /payments/verify` |
| Invoices | `GET /invoices/{orderId}` |
| Disputes | `POST /orders/{id}/disputes`, `GET /disputes/mine` |
| Reviews | `POST /reviews` |
| Messages | `GET /messages/conversations`, `GET /messages/book/{bookId}/user/{userId}` |
| Reports | `POST /reports` |
| Admin | `/admin/users`, `/admin/reports`, `/admin/disputes`, `/admin/books`, `/admin/settings` |

---

## 🗺️ Roadmap

- [ ] College/course-branch relevance filter on search
- [ ] Multilingual UI (Hindi/Marathi)
- [ ] PWA / offline browsing support
- [ ] Automated test suite + CI pipeline

---

## 🤝 Contributing

This project is built and maintained as a team effort, split by module:

| Area | Modules |
|---|---|
| Marketplace & Listings | `book`, `search` |
| Transactions | `cart`, `order`, `payment`, `invoice` |
| Trust & Safety | `auth`, `report`, `dispute` |
| Admin & Community | `admin`, `review`, `message`, `notification` |

Pull requests should target a single module where possible and include a short description of what changed and why.

---

## 📄 License

This project is for educational purposes.
