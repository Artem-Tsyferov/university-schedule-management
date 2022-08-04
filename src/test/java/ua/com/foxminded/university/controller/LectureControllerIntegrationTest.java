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
import ua.com.foxminded.university.dto.LectureDTO;
import ua.com.foxminded.university.models.Lecture;
import ua.com.foxminded.university.models.enums.UserRole;

import java.time.LocalDate;
import java.time.LocalTime;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LectureControllerIntegrationTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Sql(scripts = {"classpath:sql/tables_filler.sql"})
    void newLecture_ShouldContainModelAndReturnView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lectures/new")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.view().name("lectures/new"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lectureDTO"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("teachers"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("groups"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("courses"));
    }

    @Test
    void newLecture_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lectures/new")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }

    @Test
    void create_ShouldContainModelAndReturnView_WhenBindingResultHasErrors() throws Exception {
        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setCourseId(1);
        lectureDTO.setGroupId(1);
        lectureDTO.setTeacherId(1);
        lectureDTO.setDate(LocalDate.of(2022, 8, 01));
        lectureDTO.setTimeOfStart(LocalTime.of(11, 11));
        lectureDTO.setTimeOfEnd(LocalTime.of(12, 22));
        mockMvc.perform(MockMvcRequestBuilders.post("/lectures")
                        .flashAttr("lectureDTO", lectureDTO))
                .andExpect(MockMvcResultMatchers.model().attribute("lectureDTO", lectureDTO))
                .andExpect(MockMvcResultMatchers.view().name("lectures/new"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/tables_filler.sql"})
    void show_ShouldContainModelAndReturnView() throws Exception {
        Lecture lecture = new Lecture();
        mockMvc.perform(MockMvcRequestBuilders.get("/lectures/1"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lecture"))
                .andExpect(MockMvcResultMatchers.view().name("lectures/show"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/tables_filler.sql"})
    void edit_ShouldContainModelsAndReturnView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lectures/1/edit")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lecture"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lectureDTO"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("teachers"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("groups"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("courses"))
                .andExpect(MockMvcResultMatchers.view().name("lectures/edit"));
    }

    @Test
    void edit_ShouldRestrictAccessIfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lectures/1/edit")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }

    @Test
    void update_ShouldReturnView_WhenBindingResultHasErrors() throws Exception {
        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setId(1);
        lectureDTO.setGroupId(1);
        lectureDTO.setCourseId(1);
        lectureDTO.setTeacherId(1);
        lectureDTO.setDate(LocalDate.of(2022, 8, 01));
        lectureDTO.setTimeOfStart(LocalTime.of(11, 11));
        lectureDTO.setTimeOfEnd(LocalTime.of(12, 22));
        Lecture lecture = new Lecture();
        mockMvc.perform(MockMvcRequestBuilders.patch("/lectures/1")
                        .flashAttr("lectureDTO", lectureDTO)
                        .flashAttr("lecture", lecture))
                .andExpect(MockMvcResultMatchers.view().name("lectures/edit"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/tables_filler.sql"})
    void delete_ShouldReturnView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/lectures/1")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"));
    }

    @Test
    void delete_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/lectures/1")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }
}