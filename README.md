# E-commerce REST API - Spring Boot Project

A complete REST API backend for an e-commerce platform built with Spring Boot 3.2.

## Features

- 🔐 **JWT Authentication** - Secure login/register with role-based access control
- 🛍️ **Product Management** - CRUD operations with category support
- 🛒 **Shopping Cart** - Add, update, remove items with stock validation
- 📦 **Order Processing** - Create orders from cart, order history
- 💳 **Stripe Integration** - Payment intent creation and webhook handling
- 📊 **H2 Database** - In-memory database for development (MySQL ready)

## Tech Stack

- Java 17
- Spring Boot 3.2.1
- Spring Security with JWT
- Spring Data JPA
- Flyway Migrations
- Stripe Java SDK
- H2 / MySQL Database
- Lombok

## Quick Start

### Prerequisites

- **Java 17, 18, 19, 20, or 21** (Required - Lombok doesn't support Java 22+ yet)
- Maven 3.8+

> **⚠️ Important**: If you have Java 22 or later installed, you'll need to install an earlier version:
> ```bash
> # Using Homebrew on macOS
> brew install openjdk@17
> export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
> 
> # Or using SDKMAN
> sdk install java 17.0.9-tem
> sdk use java 17.0.9-tem
> ```

### Run the Application

```bash
cd /Users/bhaskar/Downloads/e-commerce

# Build the project
./mvnw clean install -DskipTests

# Run the application
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

### Default Users

| Email | Password | Role |
|-------|----------|------|
| admin@ecommerce.com | admin123 | ADMIN |
| user@ecommerce.com | user123 | USER |

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and get JWT |

### Products (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | List all products |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/search?q=` | Search products |
| GET | `/api/products/category/{id}` | Products by category |

### Categories (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | List all categories |
| GET | `/api/categories/{id}` | Get category by ID |

### Cart (Authenticated)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/cart` | Get user's cart |
| POST | `/api/cart/items` | Add item to cart |
| PUT | `/api/cart/items/{id}` | Update quantity |
| DELETE | `/api/cart/items/{id}` | Remove item |

### Orders (Authenticated)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/orders` | Get user's orders |
| GET | `/api/orders/{id}` | Get order by ID |
| POST | `/api/orders` | Create order from cart |

### Payments (Authenticated)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/payments/create-intent` | Create Stripe payment |
| POST | `/api/payments/webhook` | Stripe webhook |

## Usage Examples

### Register

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@ecommerce.com",
    "password": "user123"
  }'
```

### Add to Cart (with JWT)

```bash
curl -X POST http://localhost:8080/api/cart/items \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'
```

## Development

### H2 Console

Access the H2 database console at: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:ecommerce_db`
- Username: `sa`
- Password: (empty)

### MySQL Profile

To use MySQL instead of H2:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

Set environment variables:
- `DB_USERNAME` - MySQL username
- `DB_PASSWORD` - MySQL password

### Stripe Configuration

Set environment variables:
- `STRIPE_API_KEY` - Your Stripe secret key
- `STRIPE_WEBHOOK_SECRET` - Webhook signing secret

## Project Structure

```
src/main/java/com/ecommerce/
├── EcommerceApplication.java
├── config/
│   ├── SecurityConfig.java
│   └── DataLoader.java
├── controllers/
│   ├── AuthController.java
│   ├── ProductController.java
│   ├── CartController.java
│   ├── OrderController.java
│   └── PaymentController.java
├── dto/
│   ├── UserDTO.java
│   ├── ProductDTO.java
│   ├── CartDTO.java
│   └── OrderDTO.java
├── entities/
│   ├── User.java
│   ├── Product.java
│   ├── Cart.java
│   └── Order.java
├── repositories/
├── security/
│   ├── JwtUtil.java
│   └── JwtAuthenticationFilter.java
├── services/
└── exceptions/
```

## License

MIT License
# e-commerce-api
