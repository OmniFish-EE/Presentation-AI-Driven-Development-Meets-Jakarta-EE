package com.example.service;

import com.example.entity.*;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.*;

@Singleton
@Startup
public class DataInitializationService {

    @PersistenceContext(unitName = "peoplePU")
    private EntityManager entityManager;

    @PostConstruct
    @Transactional
    public void initializeData() {
        // Check if data already exists
        if (entityManager.createQuery("SELECT COUNT(p) FROM Person p", Long.class).getSingleResult() > 0) {
            return; // Data already initialized
        }

        // Create groups
        Group teachersGroup = new Group("Teachers", "Teachers group");
        Group studentsGroup = new Group("Students", "Students group");
        entityManager.persist(teachersGroup);
        entityManager.persist(studentsGroup);

        // Create subjects
        Subject english = new Subject("English");
        Subject math = new Subject("Math");
        Subject physicalEd = new Subject("Physical education");
        Subject biology = new Subject("Biology");
        Subject spanish = new Subject("Spanish");
        Subject history = new Subject("History");
        
        entityManager.persist(english);
        entityManager.persist(math);
        entityManager.persist(physicalEd);
        entityManager.persist(biology);
        entityManager.persist(spanish);
        entityManager.persist(history);

        // Create grades with subjects
        Grade grade1 = new Grade(1);
        entityManager.persist(grade1);
        grade1.getSubjects().addAll(Arrays.asList(english, math, physicalEd));
        entityManager.merge(grade1);

        Grade grade2 = new Grade(2);
        entityManager.persist(grade2);
        grade2.getSubjects().addAll(Arrays.asList(english, math, physicalEd, biology));
        entityManager.merge(grade2);

        Grade grade3 = new Grade(3);
        entityManager.persist(grade3);
        grade3.getSubjects().addAll(Arrays.asList(english, math, spanish, physicalEd, biology, history));
        entityManager.merge(grade3);
        
        // Flush to ensure grade-subject relationships are persisted
        entityManager.flush();

        // Create 10 teachers
        String[] teacherNames = {
            "John Smith", "Mary Johnson", "David Brown", "Sarah Davis", "Michael Wilson",
            "Lisa Anderson", "Robert Taylor", "Jennifer Thomas", "William Jackson", "Patricia White"
        };

        List<Person> teachers = new ArrayList<>();
        for (String name : teacherNames) {
            String[] parts = name.split(" ");
            Person teacher = new Person(parts[0], parts[1]);
            teacher.setRole(PersonRole.TEACHER);
            teacher.getGroups().add(teachersGroup);
            entityManager.persist(teacher);
            teachers.add(teacher);
        }

        // Assign subjects to teachers randomly
        List<Subject> allSubjects = Arrays.asList(english, math, physicalEd, biology, spanish, history);
        Random random = new Random();
        for (Person teacher : teachers) {
            // Each teacher gets 2-3 subjects (but not more than available)
            int numSubjects = Math.min(2 + random.nextInt(2), allSubjects.size());
            Set<Subject> teacherSubjects = new HashSet<>();
            int attempts = 0;
            while (teacherSubjects.size() < numSubjects && attempts < 50) {
                teacherSubjects.add(allSubjects.get(random.nextInt(allSubjects.size())));
                attempts++;
            }
            teacher.setSubjects(teacherSubjects);
            entityManager.merge(teacher);
        }

        // Create 2 students per grade
        String[] studentNames = {
            "Alice Cooper", "Bob Miller", "Charlie Wilson", "Diana Prince", "Emma Stone", "Frank Castle"
        };

        List<Grade> grades = Arrays.asList(grade1, grade2, grade3);
        for (int i = 0; i < studentNames.length; i++) {
            String[] parts = studentNames[i].split(" ");
            Person student = new Person(parts[0], parts[1]);
            student.setRole(PersonRole.STUDENT);
            student.setGrade(grades.get(i / 2)); // 2 students per grade
            student.getGroups().add(studentsGroup);
            entityManager.persist(student);
        }

        // Generate schedule
        generateSchedule(teachers, Arrays.asList(grade1, grade2, grade3));
    }

    private void generateSchedule(List<Person> teachers, List<Grade> grades) {
        Random random = new Random();
        
        for (Grade grade : grades) {
            for (Subject subject : grade.getSubjects()) {
                // Find teachers who can teach this subject
                List<Person> availableTeachers = teachers.stream()
                    .filter(t -> t.getSubjects().contains(subject))
                    .toList();
                
                if (!availableTeachers.isEmpty()) {
                    // Schedule 2-3 classes per week for each subject
                    int classesPerWeek = 2 + random.nextInt(2);
                    Set<String> usedSlots = new HashSet<>();
                    
                    for (int i = 0; i < classesPerWeek; i++) {
                        int attempts = 0;
                        boolean scheduled = false;
                        
                        while (!scheduled && attempts < 50) { // Limit attempts to prevent infinite loop
                            Person teacher = availableTeachers.get(random.nextInt(availableTeachers.size()));
                            DayOfWeek day = DayOfWeek.values()[random.nextInt(5)]; // Monday-Friday
                            int timeSlot = 1 + random.nextInt(8); // 1-8 periods
                            
                            String slotKey = day + "-" + timeSlot;
                            if (!usedSlots.contains(slotKey) && !isTeacherBusy(teacher, day, timeSlot)) {
                                Schedule schedule = new Schedule(teacher, subject, grade, day, timeSlot);
                                entityManager.persist(schedule);
                                usedSlots.add(slotKey);
                                scheduled = true;
                            }
                            attempts++;
                        }
                    }
                }
            }
        }
    }

    private boolean isTeacherBusy(Person teacher, DayOfWeek day, int timeSlot) {
        return entityManager.createQuery(
            "SELECT COUNT(s) FROM Schedule s WHERE s.teacher = :teacher AND s.dayOfWeek = :day AND s.timeSlot = :slot",
            Long.class)
            .setParameter("teacher", teacher)
            .setParameter("day", day)
            .setParameter("slot", timeSlot)
            .getSingleResult() > 0;
    }
}
