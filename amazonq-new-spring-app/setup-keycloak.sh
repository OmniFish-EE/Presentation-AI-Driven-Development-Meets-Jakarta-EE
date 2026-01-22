#!/bin/bash

echo "Setting up Keycloak for School Management System..."

# Wait for Keycloak to be ready
echo "Waiting for Keycloak to start..."
until curl -f http://localhost:8080; do
    sleep 5
done

# Get admin token
echo "Getting admin token..."
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8080/realms/master/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin" \
  -d "password=admin" \
  -d "grant_type=password" \
  -d "client_id=admin-cli" | jq -r '.access_token')

# Create realm
echo "Creating school-management realm..."
curl -s -X POST http://localhost:8080/admin/realms \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "realm": "school-management",
    "enabled": true,
    "displayName": "School Management System"
  }'

# Create client
echo "Creating school-management client..."
curl -s -X POST http://localhost:8080/admin/realms/school-management/clients \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "school-management",
    "secret": "school-management-secret",
    "enabled": true,
    "clientAuthenticatorType": "client-secret",
    "redirectUris": ["http://localhost:8081/*"],
    "webOrigins": ["http://localhost:8081"],
    "protocol": "openid-connect",
    "publicClient": false,
    "standardFlowEnabled": true,
    "serviceAccountsEnabled": true,
    "authorizationServicesEnabled": false
  }'

# Create admin role
echo "Creating admin role..."
curl -s -X POST http://localhost:8080/admin/realms/school-management/roles \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "admin",
    "description": "Administrator role"
  }'

# Create admin user
echo "Creating admin user..."
curl -s -X POST http://localhost:8080/admin/realms/school-management/users \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "enabled": true,
    "firstName": "Admin",
    "lastName": "User",
    "email": "admin@school.com",
    "credentials": [{
      "type": "password",
      "value": "admin",
      "temporary": false
    }]
  }'

# Get admin user ID
echo "Getting admin user ID..."
ADMIN_USER_ID=$(curl -s -X GET "http://localhost:8080/admin/realms/school-management/users?username=admin" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.[0].id')

# Get admin role
echo "Getting admin role..."
ADMIN_ROLE=$(curl -s -X GET http://localhost:8080/admin/realms/school-management/roles/admin \
  -H "Authorization: Bearer $ADMIN_TOKEN")

# Assign admin role to admin user
echo "Assigning admin role to admin user..."
curl -s -X POST http://localhost:8080/admin/realms/school-management/users/$ADMIN_USER_ID/role-mappings/realm \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d "[$ADMIN_ROLE]"

echo "Keycloak setup completed!"
echo "Admin user: admin / admin"
echo "Keycloak Admin Console: http://localhost:8080/admin"
echo "Application: http://localhost:8081"
