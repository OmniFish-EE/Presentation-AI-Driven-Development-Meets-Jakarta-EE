package com.example.service;

import com.example.entity.Group;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Group entity operations following business logic separation
 */
@ApplicationScoped
@Transactional
public class GroupService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Group> findAll() {
        return entityManager.createQuery("SELECT g FROM Group g ORDER BY g.name", Group.class)
                .getResultList();
    }

    public Optional<Group> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(Group.class, id));
    }

    public Group create(@Valid Group group) {
        validateGroupData(group);
        entityManager.persist(group);
        entityManager.flush(); // Ensure ID is generated
        return group;
    }

    public Optional<Group> update(@NotNull Long id, @Valid Group groupData) {
        return findById(id).map(existing -> {
            validateGroupData(groupData);
            existing.setName(groupData.getName().trim());
            existing.setDescription(groupData.getDescription() != null ? 
                groupData.getDescription().trim() : null);
            return existing;
        });
    }

    public boolean delete(@NotNull Long id) {
        return findById(id).map(group -> {
            // Clear relationships before deletion
            group.getPeople().forEach(person -> person.getGroups().remove(group));
            entityManager.remove(group);
            return true;
        }).orElse(false);
    }

    private void validateGroupData(Group group) {
        if (group.getName() == null || group.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Group name is required");
        }
        if (group.getName().trim().length() > 100) {
            throw new IllegalArgumentException("Group name must not exceed 100 characters");
        }
        if (group.getDescription() != null && group.getDescription().length() > 500) {
            throw new IllegalArgumentException("Description must not exceed 500 characters");
        }
    }
}
