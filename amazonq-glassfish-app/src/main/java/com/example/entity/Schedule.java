package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Person teacher;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @NotNull
    @Column(name = "time_slot", nullable = false)
    private Integer timeSlot; // 1-8 for 8 periods per day

    public Schedule() {}

    public Schedule(Person teacher, Subject subject, Grade grade, DayOfWeek dayOfWeek, Integer timeSlot) {
        this.teacher = teacher;
        this.subject = subject;
        this.grade = grade;
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Person getTeacher() { return teacher; }
    public void setTeacher(Person teacher) { this.teacher = teacher; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public Grade getGrade() { return grade; }
    public void setGrade(Grade grade) { this.grade = grade; }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public Integer getTimeSlot() { return timeSlot; }
    public void setTimeSlot(Integer timeSlot) { this.timeSlot = timeSlot; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Schedule)) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(id, schedule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Schedule{id=%d, teacher=%s, subject=%s, grade=%d, day=%s, slot=%d}", 
            id, teacher != null ? teacher.getFirstName() : null, 
            subject != null ? subject.getName() : null,
            grade != null ? grade.getGradeNumber() : null, dayOfWeek, timeSlot);
    }
}
