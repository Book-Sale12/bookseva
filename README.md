# BookSeva - Academic Book Marketplace

BookSeva is a student-to-student used academic-book marketplace built with React (Vite) and Spring Boot.

## Prerequisites

- Node.js 18+
- Java 17+
- MySQL 8+
- Maven

## Backend Setup

1. Navigate to the `backend` directory.
2. Create your `.env` file by copying the provided example:
   ```bash
   cp .env.example .env
   ```
3. Update `.env` with your local database credentials and other necessary secrets (e.g., Mail, JWT, Cloudinary, Razorpay).
4. Run the Spring Boot application using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

## Frontend Setup

1. Navigate to the `frontend` directory.
2. Install dependencies:
   ```bash
   npm install
   ```
3. Create your `.env` file by copying the example:
   ```bash
   cp .env.example .env
   ```
4. Fill in the missing values in `.env` (like Razorpay Key ID and Cloudinary Cloud Name).
5. Start the development server:
   ```bash
   npm run dev
   ```

## Database

The backend uses Hibernate with `ddl-auto: update`, so the database schema will be automatically generated/updated when the application starts. Create a database named `bookseva` in MySQL before starting the application:

```sql
CREATE DATABASE bookseva;
```
