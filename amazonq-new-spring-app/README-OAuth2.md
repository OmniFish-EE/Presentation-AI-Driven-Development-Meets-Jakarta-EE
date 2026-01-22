# School Management System with OAuth2 Authentication

This application now includes OAuth2 authentication using Keycloak, with role-based access control.

## 🔐 Authentication Setup

### Components
- **Keycloak**: OAuth2/OpenID Connect provider (port 8080)
- **PostgreSQL**: Database for both application and Keycloak (port 5433)
- **Spring Boot App**: School management system (port 8081)

### Default Credentials
- **Admin User**: `admin` / `admin` (role: `admin`)
- **Keycloak Admin**: `admin` / `admin`

## 🚀 Quick Start

1. **Start the system:**
   ```bash
   ./start.sh
   ```

2. **Access the application:**
   - Application: http://localhost:8081
   - Keycloak Admin: http://localhost:8080/admin

3. **Login Flow:**
   - Visit http://localhost:8081
   - Click "Login with Keycloak"
   - Use credentials: `admin` / `admin`
   - You'll be redirected back to the application

## 🔒 Security Features

### Role-Based Access Control
- **Admin Role Required**: All API endpoints and UI pages require `ADMIN` role
- **Protected Endpoints**: All `/api/*` endpoints are secured
- **Protected Pages**: Main application UI requires authentication

### OAuth2 Configuration
- **Provider**: Keycloak (school-management realm)
- **Client**: school-management
- **Scopes**: openid, profile, email
- **Flow**: Authorization Code with PKCE

### Security Annotations
All controllers use `@PreAuthorize("hasRole('ADMIN')")` for method-level security.

## 📋 API Endpoints (All require ADMIN role)

### People Management
- `GET /api/people` - List all people
- `POST /api/people` - Create person
- `PUT /api/people/{id}` - Update person
- `DELETE /api/people/{id}` - Delete person

### Groups Management
- `GET /api/groups` - List all groups
- `POST /api/groups` - Create group
- `PUT /api/groups/{id}` - Update group
- `DELETE /api/groups/{id}` - Delete group

### Subjects Management
- `GET /api/subjects` - List all subjects
- `POST /api/subjects` - Create subject
- `PUT /api/subjects/{id}` - Update subject
- `DELETE /api/subjects/{id}` - Delete subject

### Grades Management
- `GET /api/grades` - List all grades
- `POST /api/grades` - Create grade
- `PUT /api/grades/{id}` - Update grade
- `DELETE /api/grades/{id}` - Delete grade

### Schedule Management
- `GET /api/schedules` - List all schedules
- `POST /api/schedules` - Create schedule
- `PUT /api/schedules/{id}` - Update schedule
- `DELETE /api/schedules/{id}` - Delete schedule

## 🛠️ Manual Setup (if needed)

If automatic setup fails, you can configure Keycloak manually:

1. **Access Keycloak Admin Console**: http://localhost:8080/admin
2. **Login**: admin / admin
3. **Create Realm**: "school-management"
4. **Create Client**: 
   - Client ID: "school-management"
   - Client Secret: "school-management-secret"
   - Valid Redirect URIs: "http://localhost:8081/*"
5. **Create Role**: "admin"
6. **Create User**: 
   - Username: "admin"
   - Password: "admin"
   - Assign "admin" role

## 🔧 Configuration Files

### Application Properties
```properties
# OAuth2 Client Configuration
spring.security.oauth2.client.registration.keycloak.client-id=school-management
spring.security.oauth2.client.registration.keycloak.client-secret=school-management-secret
spring.security.oauth2.client.provider.keycloak.issuer-uri=http://localhost:8080/realms/school-management

# Resource Server Configuration
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/school-management
```

### Security Configuration
- JWT token validation
- Role extraction from Keycloak realm roles
- Method-level security with `@PreAuthorize`
- OAuth2 login flow configuration

## 🧪 Testing Authentication

### Test with curl (after getting token):
```bash
# Get token from Keycloak
TOKEN=$(curl -s -X POST http://localhost:8080/realms/school-management/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin" \
  -d "password=admin" \
  -d "grant_type=password" \
  -d "client_id=school-management" \
  -d "client_secret=school-management-secret" | jq -r '.access_token')

# Use token to access API
curl -H "Authorization: Bearer $TOKEN" http://localhost:8081/api/people
```

## 🐳 Docker Services

- **PostgreSQL**: Hosts both application and Keycloak databases
- **Keycloak**: OAuth2 provider with development mode enabled
- **Application**: Spring Boot with OAuth2 security

## 🔍 Troubleshooting

1. **Port conflicts**: Ensure ports 8080, 8081, and 5433 are available
2. **Database issues**: Check PostgreSQL container is running
3. **Keycloak setup**: Run `./setup-keycloak-simple.sh` if realm is missing
4. **Token issues**: Check Keycloak realm configuration matches application properties

## 📚 Architecture

The application follows enterprise security patterns:
- **OAuth2/OpenID Connect** for authentication
- **JWT tokens** for stateless authorization
- **Role-based access control** (RBAC)
- **Method-level security** with Spring Security
- **Proper transaction boundaries** with security context
- **Input validation** with security considerations
