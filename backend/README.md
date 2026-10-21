# Dreadbots Project Management Backend

This directory contains a lightweight Spring Boot backend for the Dreadbots project management app.

## Stack
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring Security
- MariaDB

## Run locally

1. Create the database and schema if you have not already done so:
   ```bash
   mysql -u root -p < ../database/schema.sql
   ```

2. Start the app:
   ```bash
   cd backend
   mvn spring-boot:run
   ```

3. The app runs on:
   - http://localhost:8080

## Default configuration

Edit `src/main/resources/application.properties` to match your local database credentials.

## Notes
- JWT auth is enabled for simple username/password authentication
- The API is designed around the same data model as the MariaDB schema
- The app purposely keeps the feature set small and easy to maintain
