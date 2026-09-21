# Employee Management API

A RESTful backend application built with **Java and Spring Boot** for managing employees with secure authentication and role-based authorization.

## Tech Stack

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA / Hibernate
* MySQL
* Maven
* JUnit / Mockito

## Features

* Employee CRUD operations
* User registration and login
* JWT-based authentication
* Role-based authorization
* MySQL database integration
* Global exception handling
* Unit and integration testing
* RESTful API architecture

## Project Structure

```text
src/main/java/com/divya/employeemanagement
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
└── service
```

## How to Run

### 1. Clone the repository

```bash
git clone https://github.com/DandaDivya/employee-management-api.git
cd employee-management-api
```

### 2. Configure MySQL

Create a MySQL database and update the database configuration in:

```text
src/main/resources/application.properties
```

### 3. Run the application

Using Maven:

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

## Testing

Run the test suite using:

```bash
mvn test
```

## API

The application provides REST endpoints for:

* User authentication
* Employee creation
* Employee retrieval
* Employee update
* Employee deletion
