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
import ua.com.foxminded.university.models.Course;
import ua.com.foxminded.university.models.Teacher;
import ua.com.foxminded.university.models.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TeacherControllerIntegrationTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void newTeacher_ShouldContainModelAndReturnView() throws Exception {
        List<Course> courses = new ArrayList<>();
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/new")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.view().name("teachers/new"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("teacher"))
                .andExpect(MockMvcResultMatchers.model().attribute("courses", courses));
    }

    @Test
    void newTeacher_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/new")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }

    @Test
    void create_ShouldContainModelAndReturnView_WhenBindingResultHasErrors() throws Exception {
        Teacher teacher = new Teacher();
        mockMvc.perform(MockMvcRequestBuilders.post("/teachers")
                        .flashAttr("teacher", teacher)
                        .param("selectedCourses", "1"))
                .andExpect(MockMvcResultMatchers.model().attribute("teacher", teacher))
                .andExpect(MockMvcResultMatchers.view().name("teachers/new"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/courses_and_teachers_filler.sql"})
    void show_ShouldContainModelAndReturnView() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setFirstName("Joe");
        teacher.setLastName("Black");
        teacher.setPersonnelNumber(111);
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/1"))
                .andExpect(MockMvcResultMatchers.model().attribute("teacher", teacher))
                .andExpect(MockMvcResultMatchers.view().name("teachers/show"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/courses_and_teachers_filler.sql"})
    void index_ShouldContainModelsAndReturnView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers"))
                .andExpect(MockMvcResultMatchers.view().name("teachers/main"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("pageNumbers"))
                .andExpect(model().size(2));
    }

    @Test
    @Sql(scripts = {"classpath:sql/courses_and_teachers_filler.sql"})
    void edit_ShouldContainModelsAndReturnView() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setFirstName("Joe");
        teacher.setLastName("Black");
        teacher.setPersonnelNumber(111);
        List<Course> courses = new ArrayList<>();
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/1/edit")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.model().attribute("teacher", teacher))
                .andExpect(MockMvcResultMatchers.model().attribute("courses", courses))
                .andExpect(MockMvcResultMatchers.view().name("teachers/edit"));
    }

    @Test
    void edit_ShouldRestrictAccessIfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/1/edit")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }

    @Test
    void update_ShouldReturnView_WhenBindingResultHasErrors() throws Exception {
        Teacher teacher = new Teacher();
        mockMvc.perform(MockMvcRequestBuilders.patch("/teachers/1")
                        .flashAttr("teacher", teacher)
                        .param("selectedCourses", "1"))
                .andExpect(MockMvcResultMatchers.view().name("teachers/edit"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/courses_and_teachers_filler.sql"})
    void delete_ShouldReturnView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/teachers/1")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/teachers"));
    }

    @Test
    void delete_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/teachers/1")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }
}