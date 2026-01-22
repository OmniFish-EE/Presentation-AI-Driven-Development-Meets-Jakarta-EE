#!/bin/bash

# Start Keycloak and PostgreSQL
echo "Starting Keycloak and PostgreSQL..."
docker compose up -d

# Wait for Keycloak to be ready
echo "Waiting for Keycloak to start..."
until curl -s http://localhost:8180/realms/school-management > /dev/null; do
    echo "Waiting for Keycloak..."
    sleep 5
done

echo "Keycloak is ready!"
echo "Access Keycloak Admin Console: http://localhost:8180/admin"
echo "Username: admin"
echo "Password: admin"
echo ""
echo "Application will be available at: http://localhost:8080/people-app/"
