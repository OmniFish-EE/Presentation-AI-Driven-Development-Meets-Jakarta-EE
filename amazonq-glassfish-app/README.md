# School Management System

A complete Jakarta EE application with OAuth authentication, demonstrating modern enterprise Java development.

## Features

- **People Management** - CRUD operations for teachers and students
- **Groups Management** - Organize people into groups
- **Schedule Management** - Weekly calendar view with time slots
- **OAuth Authentication** - Keycloak integration with admin/admin login
- **REST API** - JSON endpoints for all operations
- **Demo Data** - Pre-loaded with 10 teachers, 6 students, 3 grades, subjects, and schedules

## Technology Stack

- **Jakarta EE 10** - JSF, CDI, JPA, EJB
- **GlassFish 7.1.0** - Application server
- **PostgreSQL 16** - Database
- **Keycloak 23.0** - OAuth/OIDC authentication
- **PrimeFaces 14.0.0** - UI components
- **Maven** - Build tool
- **Docker Compose** - Container orchestration

## Quick Start

### Start Everything
```bash
./start-all.sh
```

This will:
1. Start PostgreSQL and Keycloak containers
2. Wait for services to be ready
3. Build and deploy the application
4. Load demo data automatically

### Access the Application
- **Main App**: http://localhost:8080/people-app/
- **Login**: admin / admin
- **Keycloak Admin**: http://localhost:8180/admin (admin/admin)

### Stop Everything
```bash
./stop-all.sh
```

## Manual Operations

### Start Database Only
```bash
docker compose up -d
```

### Deploy Application Only
```bash
./deploy.sh
```

### Stop GlassFish Only
```bash
target/glassfish7/bin/asadmin stop-domain
```

## Demo Data

The system automatically creates:
- **10 Teachers** with subjects assigned by grade
- **6 Students** (2 per grade)
- **3 Grades** (Grade 1, 2, 3)
- **Subjects** per grade (English, Math, PE + Biology for Grade 2+ + Spanish/History for Grade 3)
- **Weekly Schedules** with conflict-free teacher assignments

## Authentication

- All pages and REST endpoints require authentication
- Login with **admin/admin**
- Session-based authentication with logout functionality
- OAuth integration with Keycloak (demonstration setup)

## Architecture

- **Session-scoped beans** for state management
- **JPA entities** with proper relationships
- **Authentication filter** for page protection
- **Service layer** with transaction management
- **RESTful API** with JSON responses
