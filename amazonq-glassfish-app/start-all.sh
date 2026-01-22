#!/bin/bash

echo "=== Starting School Management System ==="

# Start database and Keycloak
echo "1. Starting PostgreSQL and Keycloak..."
docker compose up -d

# Wait for PostgreSQL to be ready
echo "2. Waiting for PostgreSQL to start..."
until docker exec people-postgres pg_isready -U postgres > /dev/null 2>&1; do
    echo "   Waiting for PostgreSQL..."
    sleep 2
done
echo "   PostgreSQL is ready!"

# Wait for Keycloak to be ready
echo "3. Waiting for Keycloak to start..."
until curl -s http://localhost:8180/realms/school-management > /dev/null 2>&1; do
    echo "   Waiting for Keycloak..."
    sleep 5
done
echo "   Keycloak is ready!"

# Build and deploy application
echo "4. Building and deploying application..."
./deploy.sh

echo ""
echo "=== System Ready! ==="
echo "🌐 Application: http://localhost:8080/people-app/"
echo "🔐 Login: admin / admin"
echo "🔧 Keycloak Admin: http://localhost:8180/admin (admin/admin)"
echo ""
echo "To stop everything: ./stop-all.sh"
