package com.example.service;

import com.example.dto.SubjectDto;
import com.example.entity.Subject;
import com.example.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SubjectService implements BaseService<Subject, Long, SubjectDto> {

    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    @Override
    @Transactional
    public Subject create(SubjectDto dto) {
        try {
            return subjectRepository.save(new Subject(dto.getName().trim()));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Subject name already exists");
        }
    }

    @Override
    @Transactional
    public Subject update(Long id, SubjectDto dto) {
        Subject subject = findByIdOrThrow(id);
        
        try {
            subject.setName(dto.getName().trim());
            return subjectRepository.save(subject);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Subject name already exists");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new IllegalArgumentException("Subject not found");
        }
        subjectRepository.deleteById(id);
    }

    private Subject findByIdOrThrow(Long id) {
        return subjectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Subject not found"));
    }
}
