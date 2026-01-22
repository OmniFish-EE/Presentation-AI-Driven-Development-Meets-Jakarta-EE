package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Subject name is required")
    @Size(max = 100, message = "Subject name must not exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY)
    private Set<Person> teachers = new HashSet<>();

    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY)
    private Set<Grade> grades = new HashSet<>();

    public Subject() {}

    public Subject(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { 
        this.name = name != null ? name.trim() : null;
    }

    public Set<Person> getTeachers() { return teachers; }
    public void setTeachers(Set<Person> teachers) { 
        this.teachers = teachers != null ? teachers : new HashSet<>();
    }

    public Set<Grade> getGrades() { return grades; }
    public void setGrades(Set<Grade> grades) { 
        this.grades = grades != null ? grades : new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Subject{id=%d, name='%s'}", id, name);
    }
}
