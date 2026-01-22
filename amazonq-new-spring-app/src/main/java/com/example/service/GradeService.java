package com.example.service;

import com.example.dto.GradeDto;
import com.example.entity.Grade;
import com.example.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GradeService implements BaseService<Grade, Long, GradeDto> {

    private final GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Override
    public List<Grade> findAll() {
        return gradeRepository.findAll();
    }

    @Override
    @Transactional
    public Grade create(GradeDto dto) {
        try {
            return gradeRepository.save(new Grade(dto.getLevel()));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Grade level already exists");
        }
    }

    @Override
    @Transactional
    public Grade update(Long id, GradeDto dto) {
        Grade grade = findByIdOrThrow(id);
        
        try {
            grade.setLevel(dto.getLevel());
            return gradeRepository.save(grade);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Grade level already exists");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!gradeRepository.existsById(id)) {
            throw new IllegalArgumentException("Grade not found");
        }
        gradeRepository.deleteById(id);
    }

    private Grade findByIdOrThrow(Long id) {
        return gradeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Grade not found"));
    }
}
