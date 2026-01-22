package com.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonService {
    private final DatabaseService db = new DatabaseService();
    
    public List<Person> findAll() {
        return db.executeQuery(
            "SELECT p.id, p.first_name, p.last_name, p.group_id, g.name as group_name " +
            "FROM people p LEFT JOIN groups g ON p.group_id = g.id ORDER BY p.id",
            this::mapPersonList
        );
    }
    
    public List<Person> findTeachers() {
        return db.executeQuery(
            "SELECT p.id, p.first_name, p.last_name, p.group_id, g.name as group_name " +
            "FROM people p JOIN groups g ON p.group_id = g.id " +
            "WHERE g.name = 'Teachers' ORDER BY p.first_name, p.last_name",
            this::mapPersonList
        );
    }
    
    public List<Person> findStudents() {
        return db.executeQuery(
            "SELECT p.id, p.first_name, p.last_name, p.group_id, g.name as group_name " +
            "FROM people p JOIN groups g ON p.group_id = g.id " +
            "WHERE g.name = 'Students' ORDER BY p.first_name, p.last_name",
            this::mapPersonList
        );
    }
    
    public Person create(Person person) {
        person.validate();
        db.executeUpdate(
            "INSERT INTO people (first_name, last_name, group_id) VALUES (?, ?, ?)",
            person.getFirstName(), person.getLastName(), person.getGroupId()
        );
        return person;
    }
    
    public Person update(Long id, Person person) {
        if (id == null || id <= 0) {
            throw new ValidationException("Invalid person ID");
        }
        person.validate();
        
        int updated = db.executeUpdate(
            "UPDATE people SET first_name = ?, last_name = ?, group_id = ? WHERE id = ?",
            person.getFirstName(), person.getLastName(), person.getGroupId(), id
        );
        
        if (updated == 0) {
            throw new ValidationException("Person not found");
        }
        
        person.setId(id);
        return person;
    }
    
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("Invalid person ID");
        }
        
        int deleted = db.executeUpdate("DELETE FROM people WHERE id = ?", id);
        if (deleted == 0) {
            throw new ValidationException("Person not found");
        }
    }
    
    private List<Person> mapPersonList(ResultSet rs) {
        List<Person> people = new ArrayList<>();
        try {
            while (rs.next()) {
                Person person = new Person(rs.getString("first_name"), rs.getString("last_name"));
                person.setId(rs.getLong("id"));
                Long groupId = rs.getObject("group_id") != null ? rs.getLong("group_id") : null;
                person.setGroupId(groupId);
                person.setGroupName(rs.getString("group_name"));
                people.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping person results", e);
        }
        return people;
    }
}
