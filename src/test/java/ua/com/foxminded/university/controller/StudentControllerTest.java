package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import ua.com.foxminded.university.models.Group;
import ua.com.foxminded.university.models.Student;
import ua.com.foxminded.university.models.enums.UserRole;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;
import ua.com.foxminded.university.utils.validator.StudentValidator;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentControllerTest {

    private MockMvc mockMvc;
    @Mock
    private StudentService studentService;
    @Mock
    private GroupService groupService;
    @Mock
    private StudentValidator studentValidator;
    @Mock
    BindingResult bindingResult;
    @InjectMocks
    private StudentController studentController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
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
        doNothing().when(studentValidator).validate(student, bindingResult);
        doNothing().when(studentService).save(student);
        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                        .flashAttr("student", student))
                .andExpect(MockMvcResultMatchers.model().attribute("student", student))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/students"));
    }

    @Test
    void create_ShouldContainModelAndReturnView_WhenBindingResultHasErrors() throws Exception {
        Student student = new Student();
        doNothing().when(studentService).save(student);
        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                        .flashAttr("student", student))
                .andExpect(MockMvcResultMatchers.model().attribute("student", student))
                .andExpect(MockMvcResultMatchers.view().name("students/new"));
    }

    @Test
    void show_ShouldContainModelAndReturnView() throws Exception {
        Group group = new Group();
        Student student = new Student(1, "Test", "Test", 1, group);
        when(studentService.findById(1)).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders.get("/students/1"))
                .andExpect(MockMvcResultMatchers.model().attribute("student", student))
                .andExpect(MockMvcResultMatchers.view().name("students/show"));
    }

    @Test
    void index_ShouldContainModelsAndReturnView() throws Exception {
        Student student = new Student();
        List<Student> students = new ArrayList<>();
        students.add(student);
        Page<Student> studentPage = new PageImpl<>(students);
        when(studentService.findPaginated(PageRequest.of(0, 5))).thenReturn(studentPage);
        mockMvc.perform(MockMvcRequestBuilders.get("/students"))
                .andExpect(MockMvcResultMatchers.view().name("students/main"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("pageNumbers"))
                .andExpect(MockMvcResultMatchers.model().attribute("studentPage", studentPage));
    }

    @Test
    void edit_ShouldContainModelsAndReturnView() throws Exception {
        Student student = new Student();
        List<Group> groups = new ArrayList<>();
        when(studentService.findById(1)).thenReturn(student);
        when(groupService.findAllGroups()).thenReturn(groups);
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
        doNothing().when(studentValidator).validate(student, bindingResult);
        doNothing().when(studentService).update(student);
        mockMvc.perform(MockMvcRequestBuilders.patch("/students/1")
                        .flashAttr("student", student))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/students"));
    }

    @Test
    void update_ShouldReturnView_WhenBindingResultHasErrors() throws Exception {
        Student student = new Student();
        doNothing().when(studentService).update(student);
        mockMvc.perform(MockMvcRequestBuilders.patch("/students/1")
                        .flashAttr("student", student))
                .andExpect(MockMvcResultMatchers.view().name("students/edit"));
    }

    @Test
    void delete_ShouldReturnView() throws Exception {
        doNothing().when(groupService).deleteById(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/students/1")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/students"));
    }

    @Test
    void delete_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/students/1")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }
}