package com.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupService {
    private final DatabaseService db = new DatabaseService();
    
    public List<Group> findAll() {
        return db.executeQuery(
            "SELECT id, name FROM groups ORDER BY name",
            this::mapGroupList
        );
    }
    
    public Group create(Group group) {
        group.validate();
        try {
            db.executeUpdate("INSERT INTO groups (name) VALUES (?)", group.getName());
            return group;
        } catch (RuntimeException e) {
            if (e.getCause() != null && e.getCause().getMessage().contains("duplicate key")) {
                throw new ValidationException("Group name already exists");
            }
            throw e;
        }
    }
    
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("Invalid group ID");
        }
        
        // Check if group has people assigned
        Integer count = db.executeQuery(
            "SELECT COUNT(*) FROM people WHERE group_id = ?",
            rs -> {
                try {
                    return rs.next() ? rs.getInt(1) : 0;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            },
            id
        );
        
        if (count > 0) {
            throw new ValidationException("Cannot delete group with assigned people");
        }
        
        int deleted = db.executeUpdate("DELETE FROM groups WHERE id = ?", id);
        if (deleted == 0) {
            throw new ValidationException("Group not found");
        }
    }
    
    private List<Group> mapGroupList(ResultSet rs) {
        List<Group> groups = new ArrayList<>();
        try {
            while (rs.next()) {
                Group group = new Group(rs.getString("name"));
                group.setId(rs.getLong("id"));
                groups.add(group);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping group results", e);
        }
        return groups;
    }
}
