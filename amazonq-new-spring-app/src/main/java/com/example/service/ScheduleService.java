package com.example.service;

import com.example.dto.ScheduleDto;
import com.example.entity.Schedule;
import com.example.entity.Person;
import com.example.entity.Subject;
import com.example.entity.Grade;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ScheduleService implements BaseService<Schedule, Long, ScheduleDto> {

    private final ScheduleRepository scheduleRepository;
    private final PersonRepository personRepository;
    private final SubjectRepository subjectRepository;
    private final GradeRepository gradeRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, PersonRepository personRepository,
                          SubjectRepository subjectRepository, GradeRepository gradeRepository) {
        this.scheduleRepository = scheduleRepository;
        this.personRepository = personRepository;
        this.subjectRepository = subjectRepository;
        this.gradeRepository = gradeRepository;
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    @Override
    @Transactional
    public Schedule create(ScheduleDto dto) {
        Schedule schedule = new Schedule();
        populateScheduleFromDto(schedule, dto);
        return scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public Schedule update(Long id, ScheduleDto dto) {
        Schedule schedule = findByIdOrThrow(id);
        populateScheduleFromDto(schedule, dto);
        return scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new IllegalArgumentException("Schedule not found");
        }
        scheduleRepository.deleteById(id);
    }

    private void populateScheduleFromDto(Schedule schedule, ScheduleDto dto) {
        schedule.setDayOfWeek(dto.getDayOfWeek());
        schedule.setTimeSlot(dto.getTimeSlot());
        schedule.setTeacher(findPersonByIdOrThrow(dto.getTeacherId()));
        schedule.setSubject(findSubjectByIdOrThrow(dto.getSubjectId()));
        schedule.setGrade(findGradeByIdOrThrow(dto.getGradeId()));
    }

    private Schedule findByIdOrThrow(Long id) {
        return scheduleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
    }

    private Person findPersonByIdOrThrow(Long id) {
        return personRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
    }

    private Subject findSubjectByIdOrThrow(Long id) {
        return subjectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Subject not found"));
    }

    private Grade findGradeByIdOrThrow(Long id) {
        return gradeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Grade not found"));
    }
}
