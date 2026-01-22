package com.example.controller;

import com.example.dto.ScheduleDto;
import com.example.entity.Schedule;
import com.example.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/schedules")
    public List<Schedule> getAllSchedules() {
        return scheduleService.findAll();
    }

    @PostMapping("/schedules")
    public Schedule createSchedule(@Valid @RequestBody ScheduleDto dto) {
        return scheduleService.create(dto);
    }

    @PutMapping("/schedules/{id}")
    public Schedule updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleDto dto) {
        return scheduleService.update(id, dto);
    }

    @DeleteMapping("/schedules/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        scheduleService.delete(id);
    }
}
