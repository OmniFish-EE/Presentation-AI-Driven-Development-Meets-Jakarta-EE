#!/bin/bash

echo "Stopping School Management System..."

# Stop Spring Boot application
echo "Stopping Spring Boot application..."
pkill -f spring-boot:run

# Stop Docker containers
echo "Stopping PostgreSQL and Keycloak containers..."
docker compose down

echo "All services stopped successfully!"
