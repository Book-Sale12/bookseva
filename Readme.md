# 📚 BookSeva

**A trust-first, peer-to-peer marketplace for students to buy, sell, and exchange used textbooks — fairly priced, identity-verified, and dispute-protected.**

BookSeva was built to solve a specific problem: new textbooks are expensive for one semester of use, and existing resale channels (WhatsApp groups, notice boards, generic marketplaces) have no identity verification, no fair pricing logic, and no protection against scams. BookSeva addresses each of these directly rather than treating them as afterthoughts.

---

## Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Database Schema](#-database-schema)
- [Getting Started](#-getting-started)
- [Configuration](#-configuration)
- [API Reference](#-api-reference)
- [Security](#-security)
- [Known Limitations](#-known-limitations--roadmap)
- [Team](#-team)

---

## ✨ Features

### Identity & Trust
- Email OTP verification gates every account before it can transact — hashed OTPs, 10-minute expiry, rate-limited attempts
- JWT authentication with short-lived access tokens and HttpOnly-cookie refresh tokens
- Forgot-password flow reusing the same OTP infrastructure with a distinct purpose flag
- Every user carries a **trust score**, starting at 100, that moves based on real outcomes — not self-reported reputation

### Marketplace
- Condition-tiered listings (Excellent / Good / Fair / Poor / Donate) with system-suggested, MRP-relative price bands
- Mandatory seller-uploaded photos of the actual copy — ISBN-fetched cover art is reference-only, never a substitute
- Full-text search with category, condition, and price-range filters, sorting, and pagination
- Optimistic locking on inventory to prevent two buyers from "winning" the same single-copy listing

### Transactions
- Cart → per-seller checkout → Razorpay payment, confirmed via both webhook and a client-triggered verification fallback
- Full order lifecycle with a 48-hour buyer confirmation window and an automatic background job that completes orders if the buyer doesn't act
- Auto-generated, downloadable PDF invoices on order completion
- Inventory is only reserved on confirmed payment — a cancelled or abandoned checkout releases the listing immediately

### Trust & Safety
- **Reports** — lightweight flagging of suspicious listings or users, reviewed by admins
- **Disputes** — buyer-raised, order-specific claims (wrong item, damaged, missing parts) restricted to the post-handover confirmation window; resolved by admins with full refund, partial refund, or dismissal — upheld disputes dock the seller's trust score and can trigger automatic suspension
- Two-way reviews after order completion, feeding the same trust score
- All resolutions trigger both in-app and email notifications to every affected party

### Communication
- In-app messaging scoped to a specific listing, restricted to the actual buyer/seller pair
- Dedicated inbox with per-conversation history and unread badges
- Polling-based live notification badges across chat and alerts

### Administration
- Dedicated admin dashboard: user management (suspend/ban/reactivate), report and dispute resolution queues, listing moderation, and runtime-editable platform settings
- Every admin action enforced server-side via role-based authorization, not just hidden UI

### Experience
- Light and dark mode
- Responsive layout with a mobile-collapsing navigation menu
- Consolidated profile hub (orders, sales, listings, account details) instead of scattered pages

---

## 🧱 Tech Stack

| Layer | Technology |
|---|---|
| **Backend** | Java 17, Spring Boot 4, Spring Security (JWT), Spring Data JPA / Hibernate, Maven |
| **Database** | MySQL |
| **Frontend** | React 19 (Vite), Tailwind CSS 4, TanStack Query, React Router v7, React Hook Form + Zod |
| **Payments** | Razorpay |
| **Media Storage** | Cloudinary |
| **Email** | Spring Mail (SMTP) |

---

## 🏗️ Architecture

A **modular monolith** — each domain is fully isolated at the package level (its own controller, service, repository, and entities), while still deploying and scaling as a single application. This gives clean separation of concerns without the operational overhead of distributed services at the current scale.

```
backend/bookseva/
├── auth/            Registration, login, OTP verification, JWT & refresh tokens
├── user/            Profile management
├── book/            Listings, condition tiers, suggested pricing
├── search/          Filtering, sorting, pagination
├── cart/            Cart items, availability checks
├── order/           Checkout, lifecycle state machine, auto-confirm scheduler
├── payment/         Razorpay integration (webhook + verification fallback)
├── invoice/         PDF generation & retrieval
├── notification/    In-app notifications + email dispatch
├── review/          Post-transaction ratings
├── message/         Listing-scoped buyer↔seller chat
├── report/          User/listing flagging
├── dispute/         Order-specific claims, resolution, trust score adjustment
├── admin/           Moderation dashboard, platform settings, audit logging
├── common/          Shared exception handling, base DTOs
└── config/          Security, CORS, JWT filter chain, admin bootstrapping

frontend/src/
├── pages/           Home, BookDetails, CreateListing, EditListing, Cart,
│                    Profile, Login, Register, VerifyOTP, ForgotPassword,
│                    Inbox, AdminDashboard
├── components/      Navbar, ChatBox, ReportModal, ReviewModal,
│                    DisputeModal, AdminRoute
└── context/         AuthContext, ThemeContext
```

---

## 🗄️ Database Schema

The schema is organized around three logical clusters:

1. **Identity** — `users`, `otp_verifications`
2. **Marketplace & Transactions** — `books`, `book_images`, `carts`, `cart_items`, `orders`, `payments`, `invoices`
3. **Trust, Safety & Communication** — `reports`, `disputes`, `dispute_evidence_urls`, `reviews`, `messages`, `notifications`, `platform_settings`, `audit_logs`

`orders` sits at the center of the schema — `payments`, `invoices`, `disputes`, and `reviews` all reference it directly, since nearly every meaningful trust and financial event in the system happens *because* an order exists.

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
cp .env.example .env      # populate with your own values — see Configuration
mvn clean install
mvn spring-boot:run
```
Runs on `http://localhost:8080`.

### 3. Frontend
```bash
cd frontend
npm install
npm run dev
```
Runs on `http://localhost:5173`.

> Start the backend first and wait for a clean startup before launching the frontend, or initial API calls will fail with connection errors.

---

## ⚙️ Configuration

### Backend (`.env`)
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

# App
ALLOWED_EMAIL_DOMAINS=
FRONTEND_ORIGIN=http://localhost:5173
PLATFORM_FEE_PERCENT=0
ADMIN_SEED_EMAIL=
ADMIN_SEED_PASSWORD=
```

### Frontend (`.env`)
```env
VITE_API_BASE_URL=http://localhost:8080/api/v1
VITE_RAZORPAY_KEY_ID=
VITE_CLOUDINARY_CLOUD_NAME=
```

> **Never commit real credentials.** Rotate any secret that has ever been hardcoded, shared, or exposed.

---

## 📡 API Reference

All routes are prefixed with `/api/v1`.

| Domain | Key Endpoints |
|---|---|
| **Auth** | `POST /auth/register`, `/auth/verify-otp`, `/auth/login`, `/auth/refresh`, `/auth/forgot-password`, `/auth/reset-password` |
| **Users** | `GET/PUT /users/me` |
| **Books** | `GET /books`, `POST /books`, `PUT /books/{id}`, `GET /books/me`, `GET /books/suggested-price` |
| **Search** | `GET /search/books` |
| **Cart** | `GET /cart`, `POST /cart/items`, `DELETE /cart/items/{id}` |
| **Orders** | `POST /orders/checkout`, `GET /orders/mine`, `PATCH /orders/{id}/status`, `POST /orders/{id}/cancel` |
| **Payments** | `POST /payments/webhook`, `POST /payments/verify` |
| **Invoices** | `GET /invoices/{orderId}` |
| **Disputes** | `POST /orders/{id}/disputes`, `GET /disputes/mine` |
| **Reviews** | `POST /reviews` |
| **Messages** | `GET /messages/conversations`, `GET /messages/book/{bookId}/user/{userId}`, `GET /messages/unread-count` |
| **Reports** | `POST /reports` |
| **Notifications** | `GET /notifications`, `GET /notifications/unread-count`, `PATCH /notifications/{id}/read` |
| **Admin** | `/admin/dashboard`, `/admin/users`, `/admin/books`, `/admin/reports`, `/admin/disputes`, `/admin/settings` |

All responses follow a consistent envelope: `{ "success": true, "data": ... }` or `{ "success": false, "error": { "code", "message" } }`.

---

## 🔒 Security

- Passwords and OTPs are BCrypt-hashed — raw values are never persisted
- JWT access tokens are short-lived; refresh tokens are stored as HttpOnly cookies, inaccessible to client-side JavaScript
- Role and status (`STUDENT`/`ADMIN`, `ACTIVE`/`SUSPENDED`/`BANNED`) are server-assigned only, never client-controlled
- Every admin endpoint enforces `@PreAuthorize("hasRole('ADMIN')")` server-side — the admin UI route guard is a UX convenience, not the actual security boundary
- Payment status is confirmed server-side via Razorpay webhook signature verification, never trusted from client redirect alone

---

## 🗺️ Known Limitations & Roadmap

Documented transparently rather than hidden:

- Admin action audit logging is modeled in the schema (`audit_logs`) but not yet automatically populated by every admin action
- Refund resolutions update internal order/trust-score state but do not yet call Razorpay's refund API — real money movement is a planned integration step
- Search does not yet default results to the student's own college/course-branch
- No automated test suite yet — this build phase prioritized functional completeness; testable service-layer separation was maintained throughout to support adding tests later without refactoring

---

## 👥 Team

Built collaboratively, split by domain ownership:

| Area | Modules |
|---|---|
| Marketplace & Listings | `book`, `search` |
| Transactions | `cart`, `order`, `payment`, `invoice` |
| Trust & Safety | `auth`, `report`, `dispute` |
| Community & Admin | `admin`, `review`, `message`, `notification` |

---

## 📄 License

Developed for educational purposes.
