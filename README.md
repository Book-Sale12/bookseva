# BookSeva

BookSeva is a full-stack educational marketplace application designed to facilitate the buying, selling, and donating of used textbooks. It aims to enhance educational accessibility in rural areas by connecting students with affordable learning resources.

## Tech Stack

### Backend
- **Java 17**
- **Spring Boot**
- **Hibernate / JPA**
- **MySQL**
- **Maven**
- **Spring Security (JWT)**
- **Integrations**: JavaMailSender (Email/OTP), Cloudinary (Image upload), Razorpay (Payments)

### Frontend
- **React**
- **Vite**
- **Tailwind CSS**
- **React Router**
- **React Query (TanStack Query)**
- **Axios**

---

## Local Setup Instructions

### 1. Environment Variables
Both the backend and frontend require `.env` files to run correctly. Do **not** commit your `.env` files to Git.

**Backend `backend/.env`**:
```env
DB_URL=jdbc:mysql://localhost:3306/bookseva
DB_USERNAME=root
DB_PASSWORD=[YOUR_MYSQL_PASSWORD]

JWT_SECRET=[YOUR_JWT_SECRET]
JWT_ACCESS_EXPIRY=900000
JWT_REFRESH_EXPIRY=604800000

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=[YOUR_EMAIL]
MAIL_PASSWORD=[YOUR_EMAIL_APP_PASSWORD]

CLOUDINARY_CLOUD_NAME=[YOUR_CLOUD_NAME]
CLOUDINARY_API_KEY=[YOUR_CLOUDINARY_API_KEY]
CLOUDINARY_API_SECRET=[YOUR_CLOUDINARY_API_SECRET]

RAZORPAY_KEY_ID=[YOUR_RAZORPAY_KEY_ID]
RAZORPAY_KEY_SECRET=[YOUR_RAZORPAY_KEY_SECRET]

FRONTEND_ORIGIN=http://localhost:5173
ALLOWED_EMAIL_DOMAINS=
PLATFORM_FEE_PERCENT=0
ADMIN_SEED_EMAIL=[YOUR_ADMIN_EMAIL]
ADMIN_SEED_PASSWORD=[YOUR_ADMIN_PASSWORD]
```

**Frontend `frontend/.env`**:
```env
VITE_API_BASE_URL=http://localhost:8080/api/v1
VITE_RAZORPAY_KEY_ID=[YOUR_RAZORPAY_KEY_ID]
VITE_CLOUDINARY_CLOUD_NAME=[YOUR_CLOUD_NAME]
```

### 2. Running the Backend
1. Ensure you have MySQL running locally and have created a database named `bookseva`.
2. Open a terminal and navigate to the `backend` directory:
   ```bash
   cd backend
   ```
3. Run the Spring Boot application using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
   *(Or just `mvn spring-boot:run` if Maven is installed globally).*
4. The backend will start on `http://localhost:8080`.

### 3. Running the Frontend
1. Open a new terminal and navigate to the `frontend` directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the Vite development server:
   ```bash
   npm run dev
   ```
4. The frontend will be available at `http://localhost:5173`.

## Collaboration
Please see [CONTRIBUTING.md](CONTRIBUTING.md) for rules on branching, committing, and creating PRs.
