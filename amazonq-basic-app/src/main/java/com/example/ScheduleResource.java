package com.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/schedules")
@Produces(MediaType.APPLICATION_JSON)
public class ScheduleResource {
    private final ScheduleService scheduleService = new ScheduleService();
    
    @GET
    public List<Schedule> getSchedules() {
        return scheduleService.findAll();
    }
    
    @GET
    @Path("/grade/{gradeId}")
    public List<Schedule> getSchedulesByGrade(@PathParam("gradeId") Long gradeId) {
        return scheduleService.findByGrade(gradeId);
    }
    
    @GET
    @Path("/teacher/{teacherId}")
    public List<Schedule> getSchedulesByTeacher(@PathParam("teacherId") Long teacherId) {
        return scheduleService.findByTeacher(teacherId);
    }
}
