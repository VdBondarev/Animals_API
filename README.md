# Hi there!!! ANIMALS_API is welcoming you!

# ANIMALS API README

## Introduction

Welcome to ANIMALS API made using Spring Boot.

## Key features
- **Uploading csv or xml files with Animal objects for persisting them into database.**
- **Splitting animals up by categories (based on their cost).**
- **Searching animals by params, such as: name, sex, type or category id.**

## Testing

- **API has both unit and integrational testing.**

## Technologies used

- **Spring Boot (v3.2.2):** A super-powerful framework for creating Java-based applications (just like this one).
- **Spring Data JPA:** Simplifies the data access layer and interactions with the database.
- **Swagger (springdoc-openapi):** Eases understanding and interaction with endpoints for other developers.
- **MapStruct (v1.5.5.Final):** Simplifies the implementation of mappings between Java bean types.
- **Liquibase:** A powerful way to ensure database-independence for project and database schema changes and control.
- **Docker:** A powerful tool for letting other developers use this application.

## Project structure

This Spring Boot application follows the most common structure with such **main layers** as:
- repository (for working with database).
- service (for business logic implementation).
- controller (for accepting clients' requests and getting responses to them).

Also, it has other **important layers** such as:
- mapper (for converting models for different purposes).
- exception (CustomGlobalExceptionHandler for getting proper messages about errors).
- dto (for managing sensitive info about models and better representation of it).
- config (config for mappers).

## Setup Instructions

To set up and run the project locally, follow these steps:

1. Clone the repository.
2. Ensure you have Java 21 installed.
3. Ensure you have Maven installed.
4. Ensure you have Docker installed.
5. Create the database configuration in the `.env` file. (put your cliend.id and client-secret for Oauth2)
6. Build the project using Maven: `mvn clean package` (it will create required jar-archive).
7. Build the image using Docker: `docker-compose build`.
8. Run the application using Docker: `docker-compose up` (to test, send requests to port pointed in your .env file as SPRING_LOCAL_PORT).
