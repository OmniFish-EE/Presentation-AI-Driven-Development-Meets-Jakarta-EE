#!/bin/bash

echo "Starting School Management System with OAuth2 Authentication..."

# Start PostgreSQL and Keycloak
echo "Starting PostgreSQL database and Keycloak..."
docker compose up -d

# Wait for PostgreSQL to be ready
echo "Waiting for database to be ready..."
until docker exec peopledb-postgres pg_isready -U postgres; do
    sleep 2
done

# Wait for Keycloak to be ready
echo "Waiting for Keycloak to be ready..."
until curl -f http://localhost:8080 2>/dev/null; do
    sleep 5
done

# Setup Keycloak
echo "Setting up Keycloak realm and users..."
./setup-keycloak.sh

# Build the application
echo "Building application..."
mvn clean package -DskipTests

# Start the SpringBoot application
echo "Starting SpringBoot application..."
nohup mvn spring-boot:run > app.log 2>&1 &
APP_PID=$!

# Wait a moment for the app to start
sleep 5

echo "Database is running on localhost:5433"
echo "Keycloak is running on localhost:8080"
echo "SpringBoot application is running on localhost:8081 (PID: $APP_PID)"
echo "Application logs: tail -f app.log"
echo ""
echo "=== Authentication Information ==="
echo "Keycloak Admin Console: http://localhost:8080/admin"
echo "Admin credentials: admin / admin"
echo ""
echo "Application: http://localhost:8081"
echo "Default user: admin / admin (role: admin)"
echo "=================================="
