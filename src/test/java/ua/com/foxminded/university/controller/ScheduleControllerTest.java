package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.models.Schedule;
import ua.com.foxminded.university.service.ScheduleService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScheduleControllerTest {

    private MockMvc mockMvc;
    @Mock
    private ScheduleService scheduleService;
    @InjectMocks
    private ScheduleController scheduleController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();
    }

    @Test
    void showScheduleForStudent_ShouldReturnView() throws Exception {
        List<Schedule> schedules = new ArrayList<>();
        LocalDate fromDate = LocalDate.now();
        when(scheduleService.findForMonthForStudent(fromDate, 1)).thenReturn(schedules);
        performRequest("/schedules/students/1");
    }

    @Test
    void showScheduleForTeacher_ShouldReturnView() throws Exception {
        List<Schedule> schedules = new ArrayList<>();
        LocalDate fromDate = LocalDate.now();
        when(scheduleService.findForMonthForTeacher(fromDate, 1)).thenReturn(schedules);
        performRequest("/schedules/teachers/1");
    }

    private void performRequest(String url) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.view().name("schedules/main"));
    }
}