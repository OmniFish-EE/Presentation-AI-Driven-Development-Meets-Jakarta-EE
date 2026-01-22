-- Groups for Teachers and Students
CREATE TABLE groups (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- People (Teachers and Students)
CREATE TABLE people (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    group_id INTEGER REFERENCES groups(id)
);

-- Grades
CREATE TABLE grades (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    level INTEGER NOT NULL
);

-- Subjects
CREATE TABLE subjects (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Grade-Subject assignments (which subjects are taught in which grades)
CREATE TABLE grade_subjects (
    id SERIAL PRIMARY KEY,
    grade_id INTEGER REFERENCES grades(id),
    subject_id INTEGER REFERENCES subjects(id),
    UNIQUE(grade_id, subject_id)
);

-- Teacher-Subject assignments (which subjects teachers can teach)
CREATE TABLE teacher_subjects (
    id SERIAL PRIMARY KEY,
    teacher_id INTEGER REFERENCES people(id),
    subject_id INTEGER REFERENCES subjects(id),
    UNIQUE(teacher_id, subject_id)
);

-- Student-Grade assignments
CREATE TABLE student_grades (
    id SERIAL PRIMARY KEY,
    student_id INTEGER REFERENCES people(id),
    grade_id INTEGER REFERENCES grades(id),
    UNIQUE(student_id, grade_id)
);

-- Time slots for scheduling
CREATE TABLE time_slots (
    id SERIAL PRIMARY KEY,
    day_of_week VARCHAR(10) NOT NULL,
    period INTEGER NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    UNIQUE(day_of_week, period)
);

-- Schedule (assigns teacher, subject, grade to time slot)
CREATE TABLE schedules (
    id SERIAL PRIMARY KEY,
    teacher_id INTEGER REFERENCES people(id),
    subject_id INTEGER REFERENCES subjects(id),
    grade_id INTEGER REFERENCES grades(id),
    time_slot_id INTEGER REFERENCES time_slots(id),
    UNIQUE(teacher_id, time_slot_id),
    UNIQUE(grade_id, time_slot_id)
);

-- Insert basic data
INSERT INTO groups (name) VALUES ('Teachers'), ('Students');

INSERT INTO grades (name, level) VALUES 
    ('Grade 1', 1),
    ('Grade 2', 2),
    ('Grade 3', 3);

INSERT INTO subjects (name) VALUES 
    ('English'),
    ('Math'),
    ('Physical Education'),
    ('Biology'),
    ('Spanish'),
    ('History');

-- Grade-Subject assignments
INSERT INTO grade_subjects (grade_id, subject_id) VALUES
    -- Grade 1: English, Math, Physical Education
    (1, 1), (1, 2), (1, 3),
    -- Grade 2: English, Math, Physical Education, Biology
    (2, 1), (2, 2), (2, 3), (2, 4),
    -- Grade 3: English, Math, Spanish, Physical Education, Biology, History
    (3, 1), (3, 2), (3, 5), (3, 3), (3, 4), (3, 6);

-- Time slots (Monday to Friday, 4 periods per day)
INSERT INTO time_slots (day_of_week, period, start_time, end_time) VALUES
    ('Monday', 1, '08:00', '09:00'),
    ('Monday', 2, '09:15', '10:15'),
    ('Monday', 3, '10:30', '11:30'),
    ('Monday', 4, '11:45', '12:45'),
    ('Tuesday', 1, '08:00', '09:00'),
    ('Tuesday', 2, '09:15', '10:15'),
    ('Tuesday', 3, '10:30', '11:30'),
    ('Tuesday', 4, '11:45', '12:45'),
    ('Wednesday', 1, '08:00', '09:00'),
    ('Wednesday', 2, '09:15', '10:15'),
    ('Wednesday', 3, '10:30', '11:30'),
    ('Wednesday', 4, '11:45', '12:45'),
    ('Thursday', 1, '08:00', '09:00'),
    ('Thursday', 2, '09:15', '10:15'),
    ('Thursday', 3, '10:30', '11:30'),
    ('Thursday', 4, '11:45', '12:45'),
    ('Friday', 1, '08:00', '09:00'),
    ('Friday', 2, '09:15', '10:15'),
    ('Friday', 3, '10:30', '11:30'),
    ('Friday', 4, '11:45', '12:45');

-- Sample Teachers (10 teachers)
INSERT INTO people (first_name, last_name, group_id) VALUES
    ('Alice', 'Johnson', 1),
    ('Bob', 'Smith', 1),
    ('Carol', 'Davis', 1),
    ('David', 'Wilson', 1),
    ('Emma', 'Brown', 1),
    ('Frank', 'Miller', 1),
    ('Grace', 'Taylor', 1),
    ('Henry', 'Anderson', 1),
    ('Ivy', 'Thomas', 1),
    ('Jack', 'Moore', 1);

-- Sample Students (2 per grade = 6 students)
INSERT INTO people (first_name, last_name, group_id) VALUES
    ('John', 'Doe', 2),
    ('Jane', 'Smith', 2),
    ('Mike', 'Johnson', 2),
    ('Sarah', 'Wilson', 2),
    ('Tom', 'Brown', 2),
    ('Lisa', 'Davis', 2);

-- Assign students to grades
INSERT INTO student_grades (student_id, grade_id) VALUES
    (11, 1), (12, 1), -- Grade 1
    (13, 2), (14, 2), -- Grade 2
    (15, 3), (16, 3); -- Grade 3

-- Assign subjects to teachers
INSERT INTO teacher_subjects (teacher_id, subject_id) VALUES
    (1, 1), (1, 2), -- Alice: English, Math
    (2, 2), (2, 4), -- Bob: Math, Biology
    (3, 1), (3, 5), -- Carol: English, Spanish
    (4, 3),         -- David: Physical Education
    (5, 4), (5, 6), -- Emma: Biology, History
    (6, 1), (6, 6), -- Frank: English, History
    (7, 2), (7, 5), -- Grace: Math, Spanish
    (8, 3),         -- Henry: Physical Education
    (9, 1), (9, 4), -- Ivy: English, Biology
    (10, 2), (10, 6); -- Jack: Math, History

-- Generate sample schedule
INSERT INTO schedules (teacher_id, subject_id, grade_id, time_slot_id) VALUES
    -- Monday
    (1, 1, 1, 1),   -- Alice teaches English to Grade 1, Monday Period 1
    (2, 2, 2, 1),   -- Bob teaches Math to Grade 2, Monday Period 1
    (3, 1, 3, 1),   -- Carol teaches English to Grade 3, Monday Period 1
    (1, 2, 1, 2),   -- Alice teaches Math to Grade 1, Monday Period 2
    (5, 4, 2, 2),   -- Emma teaches Biology to Grade 2, Monday Period 2
    (7, 5, 3, 2),   -- Grace teaches Spanish to Grade 3, Monday Period 2
    (4, 3, 1, 3),   -- David teaches PE to Grade 1, Monday Period 3
    (2, 4, 2, 3),   -- Bob teaches Biology to Grade 2, Monday Period 3
    (10, 2, 3, 3),  -- Jack teaches Math to Grade 3, Monday Period 3
    -- Tuesday
    (6, 1, 1, 5),   -- Frank teaches English to Grade 1, Tuesday Period 1
    (7, 2, 2, 5),   -- Grace teaches Math to Grade 2, Tuesday Period 1
    (5, 6, 3, 5),   -- Emma teaches History to Grade 3, Tuesday Period 1
    (10, 2, 1, 6),  -- Jack teaches Math to Grade 1, Tuesday Period 2
    (3, 1, 2, 6),   -- Carol teaches English to Grade 2, Tuesday Period 2
    (2, 4, 3, 6),   -- Bob teaches Biology to Grade 3, Tuesday Period 2
    (8, 3, 2, 7),   -- Henry teaches PE to Grade 2, Tuesday Period 3
    (4, 3, 3, 7);   -- David teaches PE to Grade 3, Tuesday Period 3
