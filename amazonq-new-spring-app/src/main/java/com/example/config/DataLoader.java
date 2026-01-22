package com.example.config;

import com.example.repository.*;
import com.example.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Arrays;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final GradeRepository gradeRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public DataLoader(PersonRepository personRepository, GroupRepository groupRepository,
                     SubjectRepository subjectRepository, GradeRepository gradeRepository,
                     ScheduleRepository scheduleRepository) {
        this.personRepository = personRepository;
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;
        this.gradeRepository = gradeRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (groupRepository.count() == 0) {
            // Create groups
            Group teachers = groupRepository.save(new Group("Teachers"));
            Group students = groupRepository.save(new Group("Students"));

            // Create subjects
            Subject english = subjectRepository.save(new Subject("English"));
            Subject math = subjectRepository.save(new Subject("Math"));
            Subject pe = subjectRepository.save(new Subject("Physical Education"));
            Subject biology = subjectRepository.save(new Subject("Biology"));
            Subject spanish = subjectRepository.save(new Subject("Spanish"));
            Subject history = subjectRepository.save(new Subject("History"));

            // Create grades with subjects
            Grade grade1 = new Grade(1);
            grade1.setSubjects(Arrays.asList(english, math, pe));
            grade1 = gradeRepository.save(grade1);

            Grade grade2 = new Grade(2);
            grade2.setSubjects(Arrays.asList(english, math, pe, biology));
            grade2 = gradeRepository.save(grade2);

            Grade grade3 = new Grade(3);
            grade3.setSubjects(Arrays.asList(english, math, spanish, pe, biology, history));
            grade3 = gradeRepository.save(grade3);

            // Create 10 teachers
            String[] teacherNames = {"Alice Johnson", "Bob Smith", "Carol Davis", "David Wilson", 
                                   "Emma Brown", "Frank Miller", "Grace Lee", "Henry Taylor", 
                                   "Ivy Anderson", "Jack Thompson"};
            
            List<Subject> allSubjects = Arrays.asList(english, math, pe, biology, spanish, history);
            Random random = new Random();

            for (String name : teacherNames) {
                String[] parts = name.split(" ");
                Person teacher = new Person(parts[0], parts[1]);
                teacher.setGroup(teachers);
                // Assign 2-3 random subjects to each teacher
                int subjectCount = 2 + random.nextInt(2);
                List<Subject> teacherSubjects = allSubjects.subList(0, subjectCount);
                teacher.setSubjects(teacherSubjects);
                personRepository.save(teacher);
            }

            // Create 2 students per grade
            String[] studentNames = {"John Doe", "Jane Smith", "Mike Johnson", "Sarah Wilson", "Tom Brown", "Lisa Davis"};
            List<Grade> grades = Arrays.asList(grade1, grade2, grade3);
            
            for (int i = 0; i < studentNames.length; i++) {
                String[] parts = studentNames[i].split(" ");
                Person student = new Person(parts[0], parts[1]);
                student.setGroup(students);
                student.setGrade(grades.get(i / 2)); // 2 students per grade
                personRepository.save(student);
            }

            // Generate simple schedule (Monday-Friday, 4 time slots per day)
            generateSchedule();
        }
    }

    private void generateSchedule() {
        List<Person> teachers = personRepository.findAll().stream()
            .filter(p -> "Teachers".equals(p.getGroup().getName()))
            .toList();
        
        List<Grade> grades = gradeRepository.findAll();
        Random random = new Random();

        for (int day = 1; day <= 5; day++) { // Monday to Friday
            for (int slot = 1; slot <= 4; slot++) { // 4 time slots per day
                for (Grade grade : grades) {
                    if (!grade.getSubjects().isEmpty()) {
                        Subject subject = grade.getSubjects().get(random.nextInt(grade.getSubjects().size()));
                        
                        // Find available teacher for this subject
                        List<Person> availableTeachers = teachers.stream()
                            .filter(t -> t.getSubjects().contains(subject))
                            .toList();
                        
                        if (!availableTeachers.isEmpty()) {
                            Person teacher = availableTeachers.get(random.nextInt(availableTeachers.size()));
                            
                            Schedule schedule = new Schedule();
                            schedule.setDayOfWeek(day);
                            schedule.setTimeSlot(slot);
                            schedule.setTeacher(teacher);
                            schedule.setSubject(subject);
                            schedule.setGrade(grade);
                            scheduleRepository.save(schedule);
                        }
                    }
                }
            }
        }
    }
}
