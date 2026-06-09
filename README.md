# Fruit & Provider Management API

A robust Spring Boot REST API built using **Domain-Driven Design (DDD)** and **Clean Architecture** principles. This project manages fruits and their associated providers, enforcing strict business rules and relational integrity.

## 🚀 Features

- **Provider Management**: Register, update, and delete providers.
- **Fruit Management**: Register fruits with magnitude conversion (Kilograms/Pounds), list all fruits, and filter fruits by provider.
- **Business Logic Enforcement**:
    - Prevention of duplicate provider names.
    - Mandatory association between Fruits and Providers.
    - **Deletion Protection**: A provider cannot be deleted if it has associated fruits (returns 400 Bad Request).
- **Validation Layer**: Strict input validation using Jakarta Bean Validation and custom Domain Value Objects.
- **Global Error Handling**: Centralized exception management returning RFC 7807 (Problem Details) compliant responses.

## 🏗️ Architecture

The project follows a **Hexagonal / Clean Architecture** structure:

- **Domain**: Pure business logic, Entity models (`Fruit`, `Provider`), and Value Objects (`Name`, `Weight`, `Country`, `Magnitude`).
- **Application**: Use Cases (interfaces) and Service implementations.
- **Infrastructure**:
    - **Persistence**: JPA Entities, Mappers, and Spring Data Repositories (MySQL).
    - **Web**: REST Controllers and DTOs (Java Records).
- **Common**: Shared components like base exceptions and cross-module Value Objects.

## 🛠️ Tech Stack

- **Java 21+** (Optimized for Records and modern syntax)
- **Spring Boot 3.x**
- **Spring Data JPA**
- **MySQL 8.0**
- **Lombok**
- **Maven**
- **JUnit 5 & Mockito** (Unit and Integration Testing)
- **Docker & Docker Compose**

---

## 🛠️ Getting Started

### Prerequisites

Ensure you have the following installed:
*   **Java 21** or higher
*   **Maven 3.9+**
*   **Docker & Docker Desktop**

---

### 1. Clone the repository
```bash
git clone https://github.com/your-username/fruit-api-h2.git
cd fruit-api-h2
```

### 2. Configure Environment

Create a `.env` file in the root directory of the project. You can use the following template:

```env
# Production Database
PROD_MYSQL_ROOT_PASSWORD=root_pass
PROD_MYSQL_DATABASE=fruit_api
PROD_MYSQL_USER=fruit_api_user
PROD_MYSQL_PASSWORD=fruit_api_pass
PROD_MYSQL_PORT=3308

# Test Database
TEST_MYSQL_ROOT_PASSWORD=root_pass
TEST_MYSQL_DATABASE=fruit_api
TEST_MYSQL_USER=fruit_api_user
TEST_MYSQL_PASSWORD=fruit_api_pass
TEST_MYSQL_PORT=3309

# Management
ADMINER_PORT=8888
```

### 3. Launch Infrastructure (Docker)
This project uses Docker to manage the MySQL databases and Adminer. Run the following command to start the containers:

```bash
# Start Production DB and Adminer
docker compose up -d db-prod adminer

# If you need to run integration tests, also start the Test DB
docker compose up -d db-test
```

> **Note:** If you change credentials in the `.env` file later, reset the volumes using `docker compose down -v`.


### 4. Database Management (Adminer)
You can visualize and manage your data by accessing Adminer in your browser:
*   **URL**: `http://localhost:8888`
*   **System**: `MySQL`
*   **Server**: `mysql-prod` (for production) or `mysql-test` (for testing)
*   **User/Pass**: Use the credentials defined in your `.env` file.

### 5. Running the Application
The application is configured with Spring Profiles to handle different environments.

**Run in Production mode:**
```bash
mvn clean spring-boot:run -Dspring-boot.run.profiles=prod
```
The API will be available at: `http://localhost:9090` (Note: Port changed to 9090 to avoid macOS system conflicts).

### 6. Running Tests
To run integration tests using the isolated `db-test` container:

```bash
mvn test -Dspring.profiles.active=test
```

**Key Test Components:**
- `FruitIntegrationTest`: Validates the full flow from Controller to Database using an active MySQL test container.
- `ProviderRestControllerTest`: Unit tests for the API layer.
- `RegisterFruitServiceTest`: Unit tests for business logic and use cases.



### 💡 Troubleshooting
*   **Port 8080 already in use**: The app is configured to run on port **9090**. If you still see this error, check if another instance of the app is running.
*   **Access Denied for user**: Ensure you have run `docker compose down -v` to clear old database volumes if you recently changed credentials in the `.env` file.
*   **Connection Refused**: Ensure the Docker containers are running (`docker ps`).

---

## 🛣️ API Endpoints

### Providers
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/providers` | Register a new provider |
| `PATCH` | `/api/providers/{id}` | Update provider details |
| `DELETE` | `/api/providers/{id}` | Delete a provider (if no fruits associated) |

### Fruits
| Method   | Endpoint | Description                          |
|:---------| :--- |:-------------------------------------|
| `POST`   | `/api/fruits` | Register a new fruit                 |
| `GET`    | `/api/fruits` | List all registered fruits           |
| `GET`    | `/api/fruits/{id}` | Get fruit details                    |
| `GET`    | `/api/fruits/provider/{providerId}` | List fruits from a specific provider |
| `PATCH`  | `/api/fruits/{id}` | Update fruit details                 |
| `DELETE` | `/api/fruits/{id}` | Delete fruit                         |


---

## 📂 Project Structure
```text
src/main/java/cat/itacademy/s04/s02/n01/
├── common/             # Shared Value Objects & Exceptions
├── fruit/
│   ├── application/    # Use Cases & Services
│   ├── domain/         # Models & Business Rules
│   └── infrastructure/ # JPA Entities, Repositories, Controllers
└── provider/
    ├── application/    # Use Cases & Services
    ├── domain/         # Models & Business Rules
    └── infrastructure/ # JPA Entities, Repositories, Controllers
```
---

## 📄 License
Distributed under the MIT License. See `LICENSE` for more information.
