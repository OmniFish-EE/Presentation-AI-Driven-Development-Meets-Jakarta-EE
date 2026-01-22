# School Management System - Architecture Diagrams

This directory contains comprehensive diagrams documenting the architecture and behavior of the School Management System.

## Class Diagram

**File:** `class-diagram.svg`

Shows the complete system architecture with:

### Layers:
- **Controllers**: REST endpoints handling HTTP requests/responses
- **Services**: Business logic layer with transaction management
- **Entities**: JPA entities representing domain objects
- **Repositories**: Data access layer interfaces
- **Utilities**: Common validation utilities

### Key Relationships:
- Controllers depend on Services (Dependency Injection)
- Services depend on Repositories and ValidationUtils
- Entities have JPA relationships (Person-Group, Person-Grade, etc.)
- All services follow the same architectural pattern

### Design Patterns Applied:
- **Dependency Inversion**: Controllers depend on service abstractions
- **Single Responsibility**: Each class has one clear purpose
- **Repository Pattern**: Data access abstraction
- **Utility Pattern**: Common validation logic centralized

## Sequence Diagrams

### 1. CREATE Operations (`sequence-create.svg`)

Demonstrates the flow for creating new entities:
1. Client sends POST request with JSON data
2. Controller delegates to Service
3. Service validates input using ValidationUtils
4. Service creates entity and saves via Repository
5. Repository persists to Database
6. Success response returned to client

**Key Features:**
- Input validation at service layer
- Transaction boundaries properly managed
- Consistent error handling

### 2. READ Operations (`sequence-read.svg`)

Shows the flow for retrieving entities:
1. Client sends GET request
2. Controller delegates to Service
3. Service calls Repository with read-only transaction
4. Repository queries Database
5. Results returned through the layers

**Key Features:**
- Read-only transactions for better performance
- Simple, efficient data retrieval
- No validation needed for read operations

### 3. UPDATE Operations (`sequence-update.svg`)

Illustrates the update process:
1. Client sends PUT request with ID and updated data
2. Controller delegates to Service
3. Service validates new input data
4. Service finds existing entity via Repository
5. Service modifies entity properties
6. Service saves updated entity
7. Updated entity returned to client

**Key Features:**
- Input validation before modification
- Entity existence verification
- Atomic update within transaction
- Optimistic locking handled by JPA

### 4. DELETE Operations (`sequence-delete.svg`)

Shows the deletion process:
1. Client sends DELETE request with entity ID
2. Controller delegates to Service
3. Service verifies entity exists
4. Service deletes via Repository
5. Repository removes from Database
6. Success response (HTTP 204) returned

**Key Features:**
- Existence check before deletion
- Proper error handling for non-existent entities
- Clean transaction boundaries

### 5. Complex Schedule Creation (`sequence-schedule-create.svg`)

Demonstrates the most complex operation involving multiple entity validations:
1. Client sends schedule creation request
2. Service validates all input parameters (dayOfWeek, timeSlot, IDs)
3. Service validates and retrieves Teacher entity
4. Service validates and retrieves Subject entity  
5. Service validates and retrieves Grade entity
6. Service creates and saves Schedule entity
7. Complete Schedule returned to client

**Key Features:**
- Multi-entity validation in single transaction
- Comprehensive input validation
- Foreign key relationship validation
- Complex business logic handling

## API Endpoints Covered

### Person Management
- `GET /api/people` - List all people
- `POST /api/people` - Create new person
- `PUT /api/people/{id}` - Update person
- `DELETE /api/people/{id}` - Delete person

### Group Management
- `GET /api/groups` - List all groups
- `POST /api/groups` - Create new group
- `PUT /api/groups/{id}` - Update group
- `DELETE /api/groups/{id}` - Delete group

### Subject Management
- `GET /api/subjects` - List all subjects
- `POST /api/subjects` - Create new subject
- `PUT /api/subjects/{id}` - Update subject
- `DELETE /api/subjects/{id}` - Delete subject

### Grade Management
- `GET /api/grades` - List all grades
- `POST /api/grades` - Create new grade
- `PUT /api/grades/{id}` - Update grade
- `DELETE /api/grades/{id}` - Delete grade

### Schedule Management
- `GET /api/schedules` - List all schedules
- `POST /api/schedules` - Create new schedule
- `PUT /api/schedules/{id}` - Update schedule
- `DELETE /api/schedules/{id}` - Delete schedule

## Best Practices Demonstrated

### Architecture
- **Layered Architecture**: Clear separation of concerns
- **Dependency Injection**: Loose coupling between components
- **Transaction Management**: Proper @Transactional usage
- **Input Validation**: Comprehensive validation at service layer

### Code Quality
- **DRY Principle**: ValidationUtils eliminates code duplication
- **SOLID Principles**: Each class has single responsibility
- **Error Handling**: Consistent exception handling across all operations
- **Security**: Input sanitization and validation

### Performance
- **Read-only Transactions**: Optimized for query operations
- **Lazy Loading**: JPA relationships loaded as needed
- **Connection Pooling**: Database connections managed efficiently

## Technology Stack

- **Spring Boot 3.2.0**: Application framework
- **Spring Data JPA**: Data access layer
- **PostgreSQL**: Database
- **Jakarta Validation**: Input validation
- **Maven**: Build management
- **Docker**: Containerization

These diagrams provide a complete view of the system architecture and demonstrate how modern Spring Boot applications should be structured following enterprise best practices.
