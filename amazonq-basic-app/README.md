# School Management System

A comprehensive school management system built with Jakarta EE and PostgreSQL for managing teachers, students, grades, subjects, and class schedules.

## Setup

1. Start the database:
   ```bash
   ./start-db.sh
   ```

2. Build the application:
   ```bash
   mvn clean package
   ```

3. Deploy the WAR file to your Jakarta EE server (e.g., GlassFish, WildFly)

## Access

- **Web UI**: http://localhost:8080/person-api/
- **REST API**: http://localhost:8080/person-api/api/

## System Features

### People Management
- **Teachers**: Manage teaching staff with subject assignments
- **Students**: Manage student enrollment and grade assignments

### Academic Structure
- **Grades**: Grade 1, Grade 2, Grade 3
- **Subjects**: 
  - Grade 1: English, Math, Physical Education
  - Grade 2: English, Math, Physical Education, Biology
  - Grade 3: English, Math, Spanish, Physical Education, Biology, History

### Scheduling System
- **Time Slots**: Monday-Friday, 4 periods per day (08:00-12:45)
- **Schedule Generation**: Automated conflict-free scheduling
- **Constraints**: 
  - Teachers cannot be in multiple classes simultaneously
  - Grades can only have one class per time slot

## REST API Endpoints

### People
- `GET /api/people` - List all people
- `GET /api/people/teachers` - List all teachers
- `GET /api/people/students` - List all students
- `POST /api/people` - Create new person
- `PUT /api/people/{id}` - Update person
- `DELETE /api/people/{id}` - Delete person

### Groups
- `GET /api/groups` - List all groups (Teachers, Students)
- `POST /api/groups` - Create new group
- `DELETE /api/groups/{id}` - Delete group

### Schedules
- `GET /api/schedules` - List all scheduled classes
- `GET /api/schedules/grade/{gradeId}` - Schedule for specific grade
- `GET /api/schedules/teacher/{teacherId}` - Schedule for specific teacher

## Sample Data

### Teachers (10)
- Alice Johnson (English, Math)
- Bob Smith (Math, Biology)
- Carol Davis (English, Spanish)
- David Wilson (Physical Education)
- Emma Brown (Biology, History)
- Frank Miller (English, History)
- Grace Taylor (Math, Spanish)
- Henry Anderson (Physical Education)
- Ivy Thomas (English, Biology)
- Jack Moore (Math, History)

### Students (6 - 2 per grade)
- **Grade 1**: John Doe, Jane Smith
- **Grade 2**: Mike Johnson, Sarah Wilson
- **Grade 3**: Tom Brown, Lisa Davis

### Schedule Sample
```json
[
  {
    "teacherName": "Alice Johnson",
    "subjectName": "English",
    "gradeName": "Grade 1",
    "dayOfWeek": "Monday",
    "period": 1,
    "startTime": "08:00:00",
    "endTime": "09:00:00"
  }
]
```

## Web Interface Features

- **Teacher Management**: Add/remove teachers, view assignments
- **Student Management**: Add/remove students, view grade assignments
- **Schedule Viewer**: Interactive grid showing weekly schedule
- **Filter Options**: View schedules by grade or teacher
- **Real-time Updates**: Immediate reflection of changes

## Best Practices Applied

### Backend
- **Input Validation**: All inputs validated with proper error messages
- **Transaction Boundaries**: Each operation uses single transaction
- **SOLID Principles**: Separation of concerns with service layer
- **DRY**: Database operations centralized in DatabaseService
- **KISS**: Simple, focused classes with single responsibilities
- **Error Handling**: Proper exception handling with meaningful messages
- **Security**: SQL injection prevention with prepared statements

### Frontend
- **Input Validation**: Client-side validation with length limits
- **XSS Prevention**: HTML escaping for all user content
- **User Feedback**: Clear error and success messages
- **Accessibility**: Proper form labels and semantic HTML
