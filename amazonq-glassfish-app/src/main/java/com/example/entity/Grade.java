package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Grade number is required")
    @Min(value = 1, message = "Grade must be at least 1")
    @Max(value = 3, message = "Grade must be at most 3")
    @Column(name = "grade_number", nullable = false, unique = true)
    private Integer gradeNumber;

    @OneToMany(mappedBy = "grade", fetch = FetchType.LAZY)
    private Set<Person> students = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "grade_subjects",
        joinColumns = @JoinColumn(name = "grade_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects = new HashSet<>();

    public Grade() {}

    public Grade(Integer gradeNumber) {
        this.gradeNumber = gradeNumber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getGradeNumber() { return gradeNumber; }
    public void setGradeNumber(Integer gradeNumber) { this.gradeNumber = gradeNumber; }

    public Set<Person> getStudents() { return students; }
    public void setStudents(Set<Person> students) { 
        this.students = students != null ? students : new HashSet<>();
    }

    public Set<Subject> getSubjects() { return subjects; }
    public void setSubjects(Set<Subject> subjects) { 
        this.subjects = subjects != null ? subjects : new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grade)) return false;
        Grade grade = (Grade) o;
        return Objects.equals(id, grade.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Grade{id=%d, gradeNumber=%d}", id, gradeNumber);
    }
}
