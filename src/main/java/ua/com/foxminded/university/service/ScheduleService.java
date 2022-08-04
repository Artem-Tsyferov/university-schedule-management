package ua.com.foxminded.university.service;

import ua.com.foxminded.university.models.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    List<Schedule> findForMonthForStudent(LocalDate fromDate, int studentId);

    List<Schedule> findForMonthForTeacher(LocalDate fromDate, int teacherId);

}
