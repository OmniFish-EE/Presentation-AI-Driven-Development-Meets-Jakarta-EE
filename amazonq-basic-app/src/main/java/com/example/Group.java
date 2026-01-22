package com.example;

public class Group {
    private Long id;
    private String name;
    
    public Group() {}
    
    public Group(String name) {
        this.name = name;
    }
    
    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Group name is required");
        }
        if (name.length() > 100) {
            throw new ValidationException("Group name must be 100 characters or less");
        }
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name != null ? name.trim() : null; }
}
