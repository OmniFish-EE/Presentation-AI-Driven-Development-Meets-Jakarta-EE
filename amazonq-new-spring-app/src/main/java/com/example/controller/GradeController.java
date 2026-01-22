package com.example.controller;

import com.example.dto.GradeDto;
import com.example.entity.Grade;
import com.example.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class GradeController {

    private final GradeService gradeService;

    @Autowired
    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping("/grades")
    public List<Grade> getAllGrades() {
        return gradeService.findAll();
    }

    @PostMapping("/grades")
    public Grade createGrade(@Valid @RequestBody GradeDto dto) {
        return gradeService.create(dto);
    }

    @PutMapping("/grades/{id}")
    public Grade updateGrade(@PathVariable Long id, @Valid @RequestBody GradeDto dto) {
        return gradeService.update(id, dto);
    }

    @DeleteMapping("/grades/{id}")
    public void deleteGrade(@PathVariable Long id) {
        gradeService.delete(id);
    }
}
