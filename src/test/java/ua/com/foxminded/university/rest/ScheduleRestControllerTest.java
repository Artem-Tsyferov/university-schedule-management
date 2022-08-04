package ua.com.foxminded.university.rest;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ua.com.foxminded.university.dto.ScheduleDTO;
import ua.com.foxminded.university.models.Schedule;
import ua.com.foxminded.university.rest.impl.ScheduleRestControllerImpl;
import ua.com.foxminded.university.service.ScheduleService;
import ua.com.foxminded.university.utils.mapper.ScheduleMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ScheduleRestControllerImpl.class)
class ScheduleRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    ScheduleService scheduleService;
    @MockBean
    ScheduleMapper scheduleMapper;

    @Test
    void findForMonthForStudent_success() throws Exception {
        LocalDate fromDate = LocalDate.now();
        Schedule schedule = createSchedule();
        List<Schedule> schedules = Arrays.asList(schedule);
        ScheduleDTO scheduleDTO = createScheduleDTO();

        when(scheduleService.findForMonthForStudent(fromDate, 1)).thenReturn(schedules);
        when(scheduleMapper.convertToScheduleDTO(schedule)).thenReturn(scheduleDTO);

        mockMvc.perform(get("/api/v1/schedules/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date", Matchers.is(schedule.getDate().toString())));
    }

    @Test
    void findForMonthForTeacher_success() throws Exception {
        LocalDate fromDate = LocalDate.now();
        Schedule schedule = createSchedule();
        List<Schedule> schedules = Arrays.asList(schedule);
        ScheduleDTO scheduleDTO = createScheduleDTO();

        when(scheduleService.findForMonthForTeacher(fromDate, 1)).thenReturn(schedules);
        when(scheduleMapper.convertToScheduleDTO(schedule)).thenReturn(scheduleDTO);

        mockMvc.perform(get("/api/v1/schedules/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date", Matchers.is(schedule.getDate().toString())));
    }

    private ScheduleDTO createScheduleDTO() {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setDate(LocalDate.of(2022, 12, 12));
        return scheduleDTO;
    }

    private Schedule createSchedule() {
        Schedule schedule = new Schedule();
        schedule.setDate(LocalDate.of(2022, 12, 12));
        return schedule;
    }
}