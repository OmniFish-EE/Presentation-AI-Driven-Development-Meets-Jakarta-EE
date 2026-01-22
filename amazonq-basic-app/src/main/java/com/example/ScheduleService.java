package com.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleService {
    private final DatabaseService db = new DatabaseService();
    
    public List<Schedule> findAll() {
        return db.executeQuery(
            "SELECT s.id, " +
            "CONCAT(p.first_name, ' ', p.last_name) as teacher_name, " +
            "sub.name as subject_name, " +
            "g.name as grade_name, " +
            "ts.day_of_week, ts.period, ts.start_time, ts.end_time " +
            "FROM schedules s " +
            "JOIN people p ON s.teacher_id = p.id " +
            "JOIN subjects sub ON s.subject_id = sub.id " +
            "JOIN grades g ON s.grade_id = g.id " +
            "JOIN time_slots ts ON s.time_slot_id = ts.id " +
            "ORDER BY ts.day_of_week, ts.period, g.level",
            this::mapScheduleList
        );
    }
    
    public List<Schedule> findByGrade(Long gradeId) {
        return db.executeQuery(
            "SELECT s.id, " +
            "CONCAT(p.first_name, ' ', p.last_name) as teacher_name, " +
            "sub.name as subject_name, " +
            "g.name as grade_name, " +
            "ts.day_of_week, ts.period, ts.start_time, ts.end_time " +
            "FROM schedules s " +
            "JOIN people p ON s.teacher_id = p.id " +
            "JOIN subjects sub ON s.subject_id = sub.id " +
            "JOIN grades g ON s.grade_id = g.id " +
            "JOIN time_slots ts ON s.time_slot_id = ts.id " +
            "WHERE s.grade_id = ? " +
            "ORDER BY ts.day_of_week, ts.period",
            this::mapScheduleList,
            gradeId
        );
    }
    
    public List<Schedule> findByTeacher(Long teacherId) {
        return db.executeQuery(
            "SELECT s.id, " +
            "CONCAT(p.first_name, ' ', p.last_name) as teacher_name, " +
            "sub.name as subject_name, " +
            "g.name as grade_name, " +
            "ts.day_of_week, ts.period, ts.start_time, ts.end_time " +
            "FROM schedules s " +
            "JOIN people p ON s.teacher_id = p.id " +
            "JOIN subjects sub ON s.subject_id = sub.id " +
            "JOIN grades g ON s.grade_id = g.id " +
            "JOIN time_slots ts ON s.time_slot_id = ts.id " +
            "WHERE s.teacher_id = ? " +
            "ORDER BY ts.day_of_week, ts.period",
            this::mapScheduleList,
            teacherId
        );
    }
    
    private List<Schedule> mapScheduleList(ResultSet rs) {
        List<Schedule> schedules = new ArrayList<>();
        try {
            while (rs.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(rs.getLong("id"));
                schedule.setTeacherName(rs.getString("teacher_name"));
                schedule.setSubjectName(rs.getString("subject_name"));
                schedule.setGradeName(rs.getString("grade_name"));
                schedule.setDayOfWeek(rs.getString("day_of_week"));
                schedule.setPeriod(rs.getInt("period"));
                schedule.setStartTime(rs.getString("start_time"));
                schedule.setEndTime(rs.getString("end_time"));
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping schedule results", e);
        }
        return schedules;
    }
}
