package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ua.com.foxminded.university.repository.ScheduleRepository;
import ua.com.foxminded.university.service.impl.ScheduleServiceImpl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

class ScheduleServiceImplTest {

    private ScheduleRepository scheduleRepository;
    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleService = new ScheduleServiceImpl(scheduleRepository);
    }

    @Test
    void findForMonthForStudent_ShouldInvokeRepoMethod() {
        LocalDate localDate = LocalDate.of(1111, 01, 10);
        List<LocalDate> dates = getDatesForMonth(localDate);
        scheduleService.findForMonthForStudent(localDate, 1);
        verify(scheduleRepository, times(1)).findSchedulesForStudent(1, dates);
    }

    @Test
    void findForMonthForStudent_ShouldGiveToRepoMethodArgument() {
        List<Object[]> schedules = new ArrayList<>();
        List<LocalDate> dates = new ArrayList<>();
        LocalDate localDate = LocalDate.of(1111, 01, 10);
        when(scheduleRepository.findSchedulesForStudent(1, dates)).thenReturn(schedules);
        scheduleService.findForMonthForStudent(localDate, 1);
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<List<LocalDate>> captor1 = ArgumentCaptor.forClass(List.class);
        verify(scheduleRepository).findSchedulesForStudent(captor.capture(), captor1.capture());
    }

    @Test
    void findForMonthForTeacher_ShouldInvokeRepoMethod() {
        LocalDate localDate = LocalDate.of(1111, 01, 10);
        List<LocalDate> dates = getDatesForMonth(localDate);
        scheduleService.findForMonthForTeacher(localDate, 1);
        verify(scheduleRepository, times(1)).findSchedulesForTeacher(1, dates);
    }

    @Test
    void findForMonthForTeacher_ShouldGiveToRepoMethodArgument() {
        List<Object[]> schedules = new ArrayList<>();
        List<LocalDate> dates = new ArrayList<>();
        LocalDate localDate = LocalDate.of(1111, 01, 10);
        when(scheduleRepository.findSchedulesForTeacher(1, dates)).thenReturn(schedules);
        scheduleService.findForMonthForTeacher(localDate, 1);
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<List<LocalDate>> captor1 = ArgumentCaptor.forClass(List.class);
        verify(scheduleRepository).findSchedulesForTeacher(captor.capture(), captor1.capture());
    }

    private List<LocalDate> getDatesForMonth(LocalDate fromDate) {
        LocalDate toDate = fromDate.plusMonths(1);
        int days = (int) fromDate.until(toDate, ChronoUnit.DAYS);
        return Stream
                .iterate(fromDate, d -> d.plusDays(1))
                .limit(days)
                .collect(Collectors.toList());
    }
}