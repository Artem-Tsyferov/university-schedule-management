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
import ua.com.foxminded.university.models.Course;
import ua.com.foxminded.university.models.Teacher;
import ua.com.foxminded.university.models.enums.UserRole;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.utils.validator.TeacherValidator;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TeacherControllerTest {

    private MockMvc mockMvc;
    @Mock
    private TeacherService teacherService;
    @Mock
    private CourseService courseService;
    @Mock
    private TeacherValidator teacherValidator;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private TeacherController teacherController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    void newTeacher_ShouldContainModelAndReturnView() throws Exception {
        List<Course> courses = new ArrayList<>();
        when(courseService.findAllCourses()).thenReturn(courses);
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
    void create_ShouldContainModelAndReturnView() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("a");
        teacher.setLastName("a");
        teacher.setPersonnelNumber(1);
        doNothing().when(teacherValidator).validate(teacher, bindingResult);
        doNothing().when(teacherService).save(teacher);
        mockMvc.perform(MockMvcRequestBuilders.post("/teachers")
                        .flashAttr("teacher", teacher)
                        .param("selectedCourses", "1"))
                .andExpect(MockMvcResultMatchers.model().attribute("teacher", teacher))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/teachers"));
    }

    @Test
    void create_ShouldContainModelAndReturnView_WhenBindingResultHasErrors() throws Exception {
        Teacher teacher = new Teacher();
        doNothing().when(teacherService).save(teacher);
        mockMvc.perform(MockMvcRequestBuilders.post("/teachers")
                        .flashAttr("teacher", teacher)
                        .param("selectedCourses", "1"))
                .andExpect(MockMvcResultMatchers.model().attribute("teacher", teacher))
                .andExpect(MockMvcResultMatchers.view().name("teachers/new"));
    }

    @Test
    void show_ShouldContainModelAndReturnView() throws Exception {
        Teacher teacher = new Teacher();
        when(teacherService.findById(1)).thenReturn(teacher);
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/1"))
                .andExpect(MockMvcResultMatchers.model().attribute("teacher", teacher))
                .andExpect(MockMvcResultMatchers.view().name("teachers/show"));
    }

    @Test
    void index_ShouldContainModelsAndReturnView() throws Exception {
        Teacher teacher = new Teacher();
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);
        Page<Teacher> teacherPage = new PageImpl<>(teachers);
        when(teacherService.findPaginated(PageRequest.of(0, 5))).thenReturn(teacherPage);
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers"))
                .andExpect(MockMvcResultMatchers.view().name("teachers/main"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("pageNumbers"))
                .andExpect(MockMvcResultMatchers.model().attribute("teacherPage", teacherPage));
    }

    @Test
    void edit_ShouldContainModelsAndReturnView() throws Exception {
        Teacher teacher = new Teacher();
        List<Course> courses = new ArrayList<>();
        when(teacherService.findById(1)).thenReturn(teacher);
        when(courseService.findAllCourses()).thenReturn(courses);
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
    void update_ShouldReturnView() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("a");
        teacher.setLastName("a");
        teacher.setPersonnelNumber(1);
        doNothing().when(teacherValidator).validate(teacher, bindingResult);
        doNothing().when(teacherService).update(teacher);
        mockMvc.perform(MockMvcRequestBuilders.patch("/teachers/1")
                        .flashAttr("teacher", teacher)
                        .param("selectedCourses", "1"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/teachers"));
    }

    @Test
    void update_ShouldReturnView_WhenBindingResultHasErrors() throws Exception {
        Teacher teacher = new Teacher();
        doNothing().when(teacherService).update(teacher);
        mockMvc.perform(MockMvcRequestBuilders.patch("/teachers/1")
                        .flashAttr("teacher", teacher)
                        .param("selectedCourses", "1"))
                .andExpect(MockMvcResultMatchers.view().name("teachers/edit"));
    }

    @Test
    void delete_ShouldReturnView() throws Exception {
        doNothing().when(teacherService).deleteById(1);
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