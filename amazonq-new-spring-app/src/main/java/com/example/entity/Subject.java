package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Subject entity representing academic subjects in the school system.
 * Follows Single Responsibility Principle by containing only subject-related data.
 */
@Entity
@Table(name = "subjects", indexes = {
    @Index(name = "idx_subject_name", columnList = "name")
})
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Subject name is required")
    @Size(min = 1, max = 255, message = "Subject name must be between 1 and 255 characters")
    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    protected Subject() {} // JPA requirement

    public Subject(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject subject = (Subject) o;
        return id != null && id.equals(subject.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Subject{id=" + id + ", name='" + name + "'}";
    }
}
