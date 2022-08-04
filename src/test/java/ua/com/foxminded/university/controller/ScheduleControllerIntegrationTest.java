package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.foxminded.university.models.Schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ScheduleControllerIntegrationTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Sql(scripts = {"classpath:sql/tables_filler.sql"})
    void showScheduleForStudent_ShouldReturnView() throws Exception {
        List<Schedule> schedules = new ArrayList<>();
        LocalDate fromDate = LocalDate.now();
        performRequest("/schedules/students/1");
    }

    @Test
    @Sql(scripts = {"classpath:sql/tables_filler.sql"})
    void showScheduleForTeacher_ShouldReturnView() throws Exception {
        List<Schedule> schedules = new ArrayList<>();
        LocalDate fromDate = LocalDate.now();
        performRequest("/schedules/teachers/1");
    }

    private void performRequest(String url) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(model().attributeExists("schedules"))
                .andExpect(MockMvcResultMatchers.view().name("schedules/main"));
    }
}