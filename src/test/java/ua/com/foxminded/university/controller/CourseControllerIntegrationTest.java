package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.foxminded.university.models.Course;
import ua.com.foxminded.university.models.enums.UserRole;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CourseControllerIntegrationTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void newCourse_ShouldContainModelAndReturnView() throws Exception {
        mockMvc.perform(get("/courses/new")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(view().name("courses/new"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    void newCourse_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(get("/courses/new")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(view().name("security/accessDenied"));
    }

    @Test
    void create_ShouldContainModelAndReturnView() throws Exception {
        Course course = new Course();
        course.setName("Test");
        mockMvc.perform(post("/courses")
                        .flashAttr("course", course))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/courses"));
    }

    @Test
    void create_ShouldContainModelAndReturnView_WhenBindingResultHasErrors() throws Exception {
        Course course = new Course();
        mockMvc.perform(post("/courses")
                        .flashAttr("course", course))
                .andExpect(model().attribute("course", course))
                .andExpect(view().name("courses/new"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/courses_filler.sql"})
    void show_ShouldContainModelAndReturnView() throws Exception {
        Course course = new Course();
        course.setId(1);
        course.setName("Mathematics");
        mockMvc.perform(get("/courses/1"))
                .andExpect(model().attribute("course", course))
                .andExpect(view().name("courses/show"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/courses_filler.sql"})
    void index_ShouldContainModelsAndReturnView() throws Exception {
        mockMvc.perform(get("/courses"))
                .andExpect(view().name("courses/main.html"))
                .andExpect(model().attributeExists("pageNumbers"))
                .andExpect(model().size(2));
    }

    @Test
    @Sql(scripts = {"classpath:sql/courses_filler.sql"})
    void edit_ShouldContainModelsAndReturnView() throws Exception {
        Course course = new Course();
        course.setId(1);
        course.setName("Mathematics");
        mockMvc.perform(get("/courses/1/edit")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(model().attribute("course", course))
                .andExpect(view().name("courses/edit"));
    }

    @Test
    void edit_ShouldRestrictAccessIfUserHasNoRights() throws Exception {
        mockMvc.perform(get("/courses/1/edit")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(view().name("security/accessDenied"));
    }

    @Test
    void update_ShouldReturnView() throws Exception {
        Course course = new Course(1, "Test", new ArrayList<>());
        mockMvc.perform(patch("/courses/1")
                        .flashAttr("course", course))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/courses"));
    }

    @Test
    void update_ShouldReturnView_WhenBindingResultHasErrors() throws Exception {
        Course course = new Course();
        mockMvc.perform(patch("/courses/1")
                        .flashAttr("course", course))
                .andExpect(view().name("courses/edit"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/courses_filler.sql"})
    void delete_ShouldReturnView() throws Exception {
        mockMvc.perform(delete("/courses/1")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/courses"));
    }

    @Test
    void delete_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(delete("/courses/1")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(view().name("security/accessDenied"));
    }

}