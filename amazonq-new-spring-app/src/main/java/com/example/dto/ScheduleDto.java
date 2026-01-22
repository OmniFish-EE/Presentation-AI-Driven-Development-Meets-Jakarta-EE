package com.example.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ScheduleDto {
    
    @NotNull(message = "Day of week is required")
    @Min(value = 1, message = "Day of week must be between 1 (Monday) and 5 (Friday)")
    @Max(value = 5, message = "Day of week must be between 1 (Monday) and 5 (Friday)")
    private Integer dayOfWeek;

    @NotNull(message = "Time slot is required")
    @Min(value = 1, message = "Time slot must be between 1 and 4")
    @Max(value = 4, message = "Time slot must be between 1 and 4")
    private Integer timeSlot;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Grade ID is required")
    private Long gradeId;

    public ScheduleDto() {}

    public ScheduleDto(Integer dayOfWeek, Integer timeSlot, Long teacherId, Long subjectId, Long gradeId) {
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
        this.teacherId = teacherId;
        this.subjectId = subjectId;
        this.gradeId = gradeId;
    }

    public Integer getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public Integer getTimeSlot() { return timeSlot; }
    public void setTimeSlot(Integer timeSlot) { this.timeSlot = timeSlot; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public Long getGradeId() { return gradeId; }
    public void setGradeId(Long gradeId) { this.gradeId = gradeId; }
}
