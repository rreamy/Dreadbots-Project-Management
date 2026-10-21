# Dreadbots Project Management

A lightweight Jira-style project management tool for a high school robotics team.

## Project structure
- `database/` contains the MariaDB schema and sample data
- `backend/` contains the Java Spring Boot API
- `frontend/` contains the React UI

## Features
- user login
- story/task management
- assignment to team members
- labels and status tracking
- simple board view
- season reset support

## Run locally with Docker Compose

From the repository root:

```bash
docker compose up --build
```

This starts:
- MariaDB on `localhost:3306`
- Spring Boot backend on `http://localhost:8080`
- React frontend on `http://localhost:80`

## Run database manually

```bash
mysql -u root -p < database/schema.sql
```

## Backend run

```bash
cd backend
mvn spring-boot:run
```

## Frontend run

```bash
cd frontend
npm install
npm run dev
```

## Notes
- This project intentionally keeps the feature set small and maintainable.
- Passwords should be replaced with real bcrypt hashes before production use.
- The database can be reset each season by recreating or dropping the tables.
