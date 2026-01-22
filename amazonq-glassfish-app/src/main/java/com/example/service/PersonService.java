package com.example.service;

import com.example.entity.Person;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Person entity operations following business logic separation
 */
@ApplicationScoped
@Transactional
public class PersonService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Person> findAll() {
        return entityManager.createQuery("SELECT p FROM Person p ORDER BY p.lastName, p.firstName", Person.class)
                .getResultList();
    }

    public Optional<Person> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(Person.class, id));
    }

    public Person create(@Valid Person person) {
        validatePersonData(person);
        entityManager.persist(person);
        entityManager.flush(); // Ensure ID is generated
        return person;
    }

    public Optional<Person> update(@NotNull Long id, @Valid Person personData) {
        return findById(id).map(existing -> {
            validatePersonData(personData);
            existing.setFirstName(personData.getFirstName().trim());
            existing.setLastName(personData.getLastName().trim());
            existing.setGroups(personData.getGroups());
            return existing;
        });
    }

    public boolean delete(@NotNull Long id) {
        return findById(id).map(person -> {
            entityManager.remove(person);
            return true;
        }).orElse(false);
    }

    private void validatePersonData(Person person) {
        if (person.getFirstName() == null || person.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (person.getLastName() == null || person.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (person.getFirstName().trim().length() > 100) {
            throw new IllegalArgumentException("First name must not exceed 100 characters");
        }
        if (person.getLastName().trim().length() > 100) {
            throw new IllegalArgumentException("Last name must not exceed 100 characters");
        }
    }
}
