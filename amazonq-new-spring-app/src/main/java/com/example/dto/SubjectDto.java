package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SubjectDto {
    
    @NotBlank(message = "Subject name is required")
    @Size(min = 1, max = 255, message = "Subject name must be between 1 and 255 characters")
    private String name;

    public SubjectDto() {}

    public SubjectDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
