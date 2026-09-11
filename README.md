## Book Store API

## 1. Introduction
This project is a RESTful API built with Spring Boot 3 for a comprehensive Book Store application. It features secure JWT-based authentication, a MySQL database configured via Docker, and functionalities for managing books, categories, shopping carts, and user orders. The application is fully containerized and includes interactive API documentation powered by Swagger/OpenAPI.

## 2. Setup and Installation

### Prerequisites
* Java 17
* Maven 3.8+
* Docker and Docker Compose

### Clone
```bash
git clone <repository-url>
cd <project-directory>
```

### Environment Variables
Create a `.env` file in the root directory of the project using the following template:

```env
MYSQLDB_USER=bookstore_user
MYSQLDB_ROOT_PASSWORD=12345678
MYSQLDB_DATABASE=db
MYSQLDB_LOCAL_PORT=3307
MYSQLDB_DOCKER_PORT=3306

SPRING_LOCAL_PORT=8088
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005
```

### Build & Run
First, build the application using Maven, then start the containerized environment using Docker Compose:

```bash
# Build the project
mvn clean package -DskipTests

# Start the application and database
docker-compose up --build -d
```

### Access
* **Base API URL:** `http://localhost:8088`
* **Swagger UI:** `http://localhost:8088/swagger-ui/index.html`
* **API Docs (JSON):** `http://localhost:8088/v3/api-docs`

## 3. API Endpoints

### Authentication
* **[GET]** `/auth/login` - Login user
* **[POST]** `/auth/registration` - Registered a new user

### Book Management
* **[GET]** `/books` - Get all books with pagination and sorting
* **[GET]** `/books/{id}` - Get book by id
* **[POST]** `/books` - Create book
* **[DELETE]** `/books/{id}` - Delete book by id
* **[PUT]** `/books/{id}` - Update book by id

### Category Management
* **[GET]** `/categories` - Retrieve all categories
* **[GET]** `/categories/{id}` - Retrieve a specific category
* **[GET]** `/categories/{id}/books` - Retrieve books by a specific category
* **[POST]** `/categories` - Create a new category
* **[PUT]** `/categories/{id}` - Update the details of a category
* **[DELETE]** `/categories/{id}` - Remove a category

### Orders
* **[POST]** `/orders` - Place order and empty ShoppingCart
* **[GET]** `/orders` - Retrieve order history
* **[GET]** `/orders/{orderId}/items` - Retrieve OrderItems by Id
* **[GET]** `/orders/{orderId}/items/{id}` - Retrieve OrderItem by Id
* **[PATCH]** `/orders/{orderId}` - Update Order status

### Shopping Cart Management
* **[GET]** `/cart` - Get shopping cart
* **[POST]** `/cart` - Add book to cart
* **[PUT]** `/cart/items/{cartItemId}` - Update book quantity
* **[DELETE]** `/cart/items/{cartItemId}` - Remove book from cart
