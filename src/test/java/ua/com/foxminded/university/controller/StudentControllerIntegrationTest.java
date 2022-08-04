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
import ua.com.foxminded.university.models.Group;
import ua.com.foxminded.university.models.Student;
import ua.com.foxminded.university.models.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class StudentControllerIntegrationTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void newStudent_ShouldContainModelAndReturnView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/students/new")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.view().name("students/new"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("groups"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("student"));
    }

    @Test
    void newStudent_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/students/new")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }

    @Test
    void create_ShouldContainModelAndReturnView() throws Exception {
        Student student = new Student();
        student.setFirstName("a");
        student.setLastName("b");
        student.setPersonnelNumber(1);
        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                        .flashAttr("student", student))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/students"));
    }

    @Test
    void create_ShouldContainModelAndReturnView_WhenBindingResultHasErrors() throws Exception {
        Student student = new Student();
        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                        .flashAttr("student", student))
                .andExpect(MockMvcResultMatchers.model().attribute("student", student))
                .andExpect(MockMvcResultMatchers.view().name("students/new"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/groups_and_students_filler.sql"})
    void show_ShouldContainModelAndReturnView() throws Exception {
        Group group = new Group();
        Student student = new Student(1, "John", "Doe", 111, group);
        mockMvc.perform(MockMvcRequestBuilders.get("/students/1"))
                .andExpect(MockMvcResultMatchers.model().attribute("student", student))
                .andExpect(MockMvcResultMatchers.view().name("students/show"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/groups_and_students_filler.sql"})
    void index_ShouldContainModelsAndReturnView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/students"))
                .andExpect(MockMvcResultMatchers.view().name("students/main"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("pageNumbers"))
                .andExpect(MockMvcResultMatchers.model().size(2));
    }

    @Test
    @Sql(scripts = {"classpath:sql/groups_and_students_filler.sql"})
    void edit_ShouldContainModelsAndReturnView() throws Exception {
        Student student = new Student();
        student.setId(1);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setPersonnelNumber(111);
        List<Group> groups = new ArrayList<>();
        mockMvc.perform(MockMvcRequestBuilders.get("/students/1/edit")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.model().attribute("student", student))
                .andExpect(MockMvcResultMatchers.model().attribute("groups", groups))
                .andExpect(MockMvcResultMatchers.view().name("students/edit"));
    }

    @Test
    void edit_ShouldRestrictAccessIfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/students/1/edit")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }

    @Test
    void update_ShouldReturnView() throws Exception {
        Student student = new Student();
        student.setFirstName("a");
        student.setLastName("b");
        student.setPersonnelNumber(1);
        mockMvc.perform(MockMvcRequestBuilders.patch("/students/1")
                        .flashAttr("student", student))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/students"));
    }

    @Test
    void update_ShouldReturnView_WhenBindingResultHasErrors() throws Exception {
        Student student = new Student();
        mockMvc.perform(MockMvcRequestBuilders.patch("/students/1")
                        .flashAttr("student", student))
                .andExpect(MockMvcResultMatchers.view().name("students/edit"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/groups_and_students_filler.sql"})
    void delete_ShouldReturnView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/students/1")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/students"));
    }

    @Test
    void delete_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/students/1")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }

}