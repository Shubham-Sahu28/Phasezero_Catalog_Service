# PhaseZero Catalog Service
A Spring Boot–based **Product Catalog Microservice** built as part of the PHASEZERO Backend Java Practical Assignment.

This service manages product data, supports searching, filtering, sorting, and inventory value computation, and uses an **H2 in-memory database** for storage.

## Features
- Add new products
- Fetch all products
- Search products (case-insensitive, substring match)
- Filter by category
- Sort products by price (ascending)
- Compute total inventory value
- Full validation & custom exception handling
- Clean 3-layer architecture (Controller → Service → Repository)
- Uniform JSON responses using `ResponseStructure<T>`

## Project Structure
```
src/main/java/.../
│
├── controller
│     └── ProductController.java
│
├── service
│     ├── ProductService.java
│     └── ProductServiceImpl.java
│
├── repository
│     └── ProductRepository.java
│
├── entity
│     └── Product.java
│
├── exception
│     ├── DuplicateDataException.java
│     ├── InvalidInputException.java
│     ├── NegativeValueException.java
│     └── NoDataFoundException.java
│
└── utils
      └── ResponseStructure.java
```
---

## Data Model

| Field       | Type    | Description |
|-------------|---------|-------------|
| id          | Long    | Primary key |
| partNumber  | String  | Unique business key |
| partName    | String  | Stored lowercase |
| category    | String  | Product category |
| price       | double  | Must be ≥ 0 |
| stock       | int     | Must be ≥ 0 |

---

## 1. Build and Run Instructions

### Requirements
- **Java** 17+
- **Maven** 3.8+
- **Database**: No external database required (uses in‑memory **H2**)

### Steps
1. Clone the repository.
2. Navigate to the project root directory.
3. Run the application using:
   bash
   mvn spring-boot:run
4. Access the API at:  
   `http://localhost:8080`

---

## 2. Project Design Overview

This project is structured into three distinct layers:

### 🔹 Controller Layer
- Handles incoming HTTP requests.
- Returns responses wrapped inside `ResponseStructure<T>`.
- Maps endpoints to corresponding service methods.

### 🔹 Service Layer
- Contains all business logic:
  - Search and filter operations.
  - Validation ( to lower case, checking for unique values, invalid input).
  - Inventory value computation.
  - All required fields must be present.
- Implements error handling using custom exceptions (e.g., `DuplicateDataException`, `NegativeValueException`,`InvalidInputException`).

### 🔹 Repository Layer
- Uses **Spring Data JPA** for persistence.
- Supports derived queries and JPQL queries.

### Entity Layer
- **Product** – JPA entity representing catalog items.

### Custom Components
- **ResponseStructure<T>** – Uniform API response wrapper for successful operation execution.
- **ErrorStructure** – Uniform API response to avoid abnormal termination & provided proper message, statuscode and error description.
- **NoDataFoundException** – Thrown when no data is found.
- **NullInputException** – Thrown when passed value is null or sent as empty in the request("").

---

## 3. H2 Database Configuration
```
spring.datasource.url=jdbc:h2:mem:catalogdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### H2 Console URL
`http://localhost:8080/h2-console` 
 
JDBC URL: `jdbc:h2:mem:catalogdb`

---

## 4. Why H2 Database?

The **H2 in‑memory database** was chosen for the following reasons:
- ✔ Lightweight and embedded.
- ✔ No installation required.
- ✔ Resets automatically on each application restart.
- ✔ Ideal for testing and evaluation.
- ✔ Fast startup with zero configuration.
- ✔ Easy debugging via H2 Console.

These qualities make H2 optimal for demonstrating API functionality in development and evaluation environments.

---

## 5. API Endpoints

### Add Product  
POST `/products`

Request:
```
{
  "partNumber": "P1001",
  "partName": "Hydraulic Filter",
  "category": "Filters",
  "price": 450.0,
  "stock": 20
}
```
### Get All Products  
GET `/products`

### Search Products  
GET `/products/search?text=filter`

### Filter by Category  
GET `/products/category/{category}`

### Sort by Price  
GET `/products/sort/price`

### Inventory Value  
GET `/products/inventory/value`

Response:
```
{
  "statusCode": 200,
  "message": "Total inventory value calculated successfully",
  "data": 45200.50
}
```

---

## 6. Custom Exceptions & Error Handling

| Case | Exception | Status Code |
|------|-----------|-------------|
| Duplicate part number | `DuplicateDataException` | 409 |
| Missing/null/empty fields | `InvalidInputException` | 400 |
| Negative values | `NegativeValueException` | 400 |
| No data found for specific fetch | `NoDataFoundException` | 404 |

The application defines several custom exceptions to ensure robust error handling:

1. **IdNotFoundException**  
   - Thrown when a requested product ID does not exist.  
   - **HTTP 404 NOT FOUND**

2. **DuplicateDataException**  
   - Thrown when attempting to create a product with an existing `partNumber`.  
   - **HTTP 409 CONFLICT**
  
3. **NegativeValueException**  
   - Thrown when `price < 0` or `stock < 0` during creation or update.  
   - **HTTP 400 BAD REQUEST**

4. **NoRecordException**  
   - Thrown when no products are found when database is empty or when search/filter results are empty.  
   - **HTTP 404 NOT FOUND**

5. **NullInputException**  
   - Thrown when the input from user is either null or empty String ("").  
   - **HTTP 400 BAD REQUEST**

6. **InvalidInputException**  
   - Thrown when for the input, user fails or forgets to provide a value.  
   - **HTTP 400 BAD REQUEST**

---

## 7. Usage Examples

### Create a Product

**Request (POST `/products`)**
json
{
  "partNumber": "P3005",
  "partName": "Air Filter",
  "category": "Filters",
  "price": 350.0,
  "stock": 40
}

**Successful Response**
{
  "message": "Product data successfully saved",
  "statusCode": 200,
  "data": {
        "category": "filters",
        "id": 1,
        "partName": "air filter",
        "partNumber": "P3005",
        "price": 350.0,
        "stock": 40
    }
}

**If any field is not provided or Invalid values are provided, the API responds with:**
{
    "error": "Null Values",
    "message": "Null value cannot be inserted, please provide a valid Input",
    "statusCode": 400
}
**OR**
{
    "error": "Duplicate Data",
    "message": "Part Number already exists: P3005",
    "statusCode": 409
}

### Fetch Product by ID

**Request (GET `/products/id/1`)**
http: GET /products/id/1

**Response**
json
{
  "message": "Product Info found based on Id:1",
  "statusCode": 200,
  "data": {
        "category": "filters",
        "id": 1,
        "partName": "air filter",
        "partNumber": "P3005",
        "price": 350.0,
        "stock": 40
    }
}

### Example: Computing Inventory Value

**Request (GET `/products/inventory/value`)**
http GET /products/inventory/value

**Response**
json
{
    "data": "Total Inventory value : 14000.0",
    "message": "Inventory value computed successfully",
    "statusCode": "200 OK"
}

---

## 8. Key Highlights
- ✅ Clean layered architecture (Controller, Service, Repository).  
- ✅ Centralized exception handling with meaningful error responses.  
- ✅ In‑memory H2 database for simplicity and fast evaluation.  
- ✅ Support for search, filter, sort and inventory value computation.  
- ✅ Uniform API response structure for consistency.  

---

## 9. Conclusion

The **Phasezero Catalog Service** provides a robust and extensible product inventory management API with clean architecture, centralized exception handling, and developer‑friendly responses. Its use of an in‑memory H2 database ensures quick setup and evaluation, while the layered design makes it easy to extend for production environments.

---
