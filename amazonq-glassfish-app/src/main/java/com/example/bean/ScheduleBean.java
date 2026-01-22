package com.example.bean;

import com.example.entity.Schedule;
import com.example.entity.DayOfWeek;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named("scheduleBean")
@RequestScoped
public class ScheduleBean implements Serializable {

    @PersistenceContext(unitName = "peoplePU")
    private EntityManager entityManager;

    private List<Schedule> schedules;

    public List<Schedule> getSchedules() {
        if (schedules == null) {
            loadSchedules();
        }
        return schedules;
    }

    public void loadSchedules() {
        schedules = entityManager.createQuery(
            "SELECT s FROM Schedule s ORDER BY s.dayOfWeek, s.timeSlot", 
            Schedule.class).getResultList();
    }

    public Map<DayOfWeek, Map<Integer, List<Schedule>>> getScheduleGrid() {
        return getSchedules().stream()
            .collect(Collectors.groupingBy(
                Schedule::getDayOfWeek,
                Collectors.groupingBy(Schedule::getTimeSlot)
            ));
    }

    public DayOfWeek[] getDays() {
        return DayOfWeek.values();
    }

    public Integer[] getTimeSlots() {
        return new Integer[]{1, 2, 3, 4, 5, 6, 7, 8};
    }

    public List<Schedule> getScheduleForSlot(DayOfWeek day, Integer timeSlot) {
        Map<DayOfWeek, Map<Integer, List<Schedule>>> grid = getScheduleGrid();
        return grid.getOrDefault(day, Map.of()).getOrDefault(timeSlot, List.of());
    }
}
