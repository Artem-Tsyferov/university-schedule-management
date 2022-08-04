package ua.com.foxminded.university.service.impl;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.models.Lecture;
import ua.com.foxminded.university.models.Schedule;
import ua.com.foxminded.university.repository.ScheduleRepository;
import ua.com.foxminded.university.service.ScheduleService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class ScheduleServiceImpl implements ScheduleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<Schedule> findForMonthForStudent(LocalDate fromDate, int studentId) {
        LOGGER.debug("Finding Schedule for Student {} for a month", studentId);
        List<Schedule> schedules = new ArrayList<>();
        try {
            List<Object[]> resultsFromQuery = scheduleRepository.findSchedulesForStudent(studentId, getDatesForMonth(fromDate));
            schedules = findSchedules(resultsFromQuery);
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to find Schedule due to", hibernateException);
        }
        return schedules;
    }

    @Override
    public List<Schedule> findForMonthForTeacher(LocalDate fromDate, int teacherId) {
        LOGGER.debug("Finding Schedule for Teacher {} for a month", teacherId);
        List<Schedule> schedules = new ArrayList<>();
        try {
            List<Object[]> resultsFromQuery = scheduleRepository.findSchedulesForTeacher(teacherId, getDatesForMonth(fromDate));
            schedules = findSchedules(resultsFromQuery);
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to find Schedule due to", hibernateException);
        }
        return schedules;
    }

    private List<LocalDate> getDatesForMonth(LocalDate fromDate) {
        LocalDate toDate = fromDate.plusMonths(1);
        int days = (int) fromDate.until(toDate, ChronoUnit.DAYS);
        return Stream
                .iterate(fromDate, d -> d.plusDays(1))
                .limit(days)
                .collect(Collectors.toList());
    }

    private List<Schedule> findSchedules(List<Object[]> resultsFromQuery) {
        List<Schedule> schedules = new ArrayList<>();
        List<Lecture> lectures = new ArrayList<>();
        for (Object[] row : resultsFromQuery){
            Schedule schedule = (Schedule) row[0];
            schedules.add(schedule);
        }
        for (Object[] row : resultsFromQuery){
            Lecture lecture = (Lecture) row[1];
            lectures.add(lecture);
        }
        for (Schedule schedule : schedules){
            List<Lecture> lecturesToAdd = new ArrayList<>();
            for(Lecture lecture : lectures){
                if(schedule.getDate().equals(lecture.getDate())){
                    lecturesToAdd.add(lecture);
                }
            }
            schedule.setLectures(lecturesToAdd);
        }
        return schedules.stream().distinct().collect(Collectors.toList());
    }
}
