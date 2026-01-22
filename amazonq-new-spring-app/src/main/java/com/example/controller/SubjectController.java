package com.example.controller;

import com.example.dto.SubjectDto;
import com.example.entity.Subject;
import com.example.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class SubjectController {

    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/subjects")
    public List<Subject> getAllSubjects() {
        return subjectService.findAll();
    }

    @PostMapping("/subjects")
    public Subject createSubject(@Valid @RequestBody SubjectDto dto) {
        return subjectService.create(dto);
    }

    @PutMapping("/subjects/{id}")
    public Subject updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectDto dto) {
        return subjectService.update(id, dto);
    }

    @DeleteMapping("/subjects/{id}")
    public void deleteSubject(@PathVariable Long id) {
        subjectService.delete(id);
    }
}
