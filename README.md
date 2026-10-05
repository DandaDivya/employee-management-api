# Employee Management API

A RESTful backend application built with **Java and Spring Boot** for managing employees with secure authentication, role-based authorization, and MySQL persistence.

The application is containerized using **Docker** and deployed to a local **Kubernetes cluster using Rancher Desktop**.

## Tech Stack

* Java 22
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA / Hibernate
* MySQL
* Maven
* Docker
* Kubernetes
* Rancher Desktop
* JUnit / Mockito

## Features

* Employee CRUD operations
* User registration and login
* JWT-based authentication
* Role-based authorization
* MySQL database integration
* Spring Data JPA / Hibernate
* Global exception handling
* Input validation
* RESTful API architecture
* Docker containerization
* Kubernetes deployment
* Kubernetes Secrets for sensitive configuration

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

## Architecture

```text
Client / Postman
       |
       v
Kubernetes NodePort
       |
       v
Kubernetes Service
       |
       v
Employee API Pod
       |
       v
Spring Boot Application
       |
       v
MySQL Database
```

## Running Locally

### 1. Clone the repository

```bash
git clone https://github.com/DandaDivya/employee-management-api.git
cd employee-management-api
```

### 2. Configure MySQL

Create a MySQL database named:

```text
employee_db
```

Configure the required database and JWT environment variables.

Do not commit passwords, JWT secrets, or other sensitive credentials to GitHub.

### 3. Run the application

Using Maven:

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

## Docker

The application is containerized using Docker.

Build the Docker image:

```bash
docker build -t employee-management-api:1.0 .
```

The application runs inside the Docker container on port `8080`.

## Kubernetes Deployment

The Docker image is deployed to a local Kubernetes cluster using Rancher Desktop.

Kubernetes resources used:

* Deployment
* Pod
* NodePort Service
* Kubernetes Secret

The application is exposed through a NodePort:

```text
http://localhost:30089
```

Sensitive configuration such as the database password and JWT secret is provided through Kubernetes Secrets rather than being stored directly in the Deployment YAML.

## API

The application provides REST endpoints for:

* User registration
* User login
* Employee creation
* Employee retrieval
* Employee update
* Employee deletion

Protected employee endpoints require JWT authentication.

## Testing

Run the test suite using:

```bash
mvn test
```

API endpoints can be tested using Postman with JWT authentication.

## Future Improvements

* Deploy to a cloud Kubernetes environment
* Add API documentation using Swagger/OpenAPI
* Add CI/CD pipeline
* Add frontend application
