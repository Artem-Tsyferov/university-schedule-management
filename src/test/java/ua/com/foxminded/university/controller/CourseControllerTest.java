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
import ua.com.foxminded.university.models.enums.UserRole;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.utils.validator.CourseValidator;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CourseControllerTest {

    private MockMvc mockMvc;
    @Mock
    private CourseService courseService;
    @Mock
    private CourseValidator courseValidator;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private CourseController courseController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    void newCourse_ShouldContainModelAndReturnView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/new")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.view().name("courses/new"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("course"));
    }

    @Test
    void newCourse_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/new")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }

    @Test
    void create_ShouldContainModelAndReturnView() throws Exception {
        Course course = new Course();
        course.setName("Test");
        doNothing().when(courseService).save(course);
        doNothing().when(courseValidator).validate(course, bindingResult);
        mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                        .flashAttr("course", course))
                .andExpect(MockMvcResultMatchers.model().attribute("course", course))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/courses"));
    }

    @Test
    void create_ShouldContainModelAndReturnView_WhenBindingResultHasErrors() throws Exception {
        Course course = new Course();
        doNothing().when(courseService).save(course);
        mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                        .flashAttr("course", course))
                .andExpect(MockMvcResultMatchers.model().attribute("course", course))
                .andExpect(MockMvcResultMatchers.view().name("courses/new"));
    }

    @Test
    void show_ShouldContainModelAndReturnView() throws Exception {
        Course course = new Course(1, "Test", new ArrayList<>());
        when(courseService.findById(1)).thenReturn(course);
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/1"))
                .andExpect(MockMvcResultMatchers.model().attribute("course", course))
                .andExpect(MockMvcResultMatchers.view().name("courses/show"));
    }

    @Test
    void index_ShouldContainModelsAndReturnView() throws Exception {
        Course course = new Course(1, "Test", new ArrayList<>());
        Course course1 = new Course(2, "Test1", new ArrayList<>());
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        courses.add(course1);
        Page<Course> coursePage = new PageImpl<>(courses);
        when(courseService.findPaginated(PageRequest.of(0, 5))).thenReturn(coursePage);
        mockMvc.perform(MockMvcRequestBuilders.get("/courses"))
                .andExpect(MockMvcResultMatchers.view().name("courses/main.html"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("pageNumbers"))
                .andExpect(MockMvcResultMatchers.model().attribute("coursePage", coursePage));
    }

    @Test
    void edit_ShouldContainModelsAndReturnView() throws Exception {
        Course course = new Course(1, "Test", new ArrayList<>());
        when(courseService.findById(1)).thenReturn(course);
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/1/edit")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.model().attribute("course", course))
                .andExpect(MockMvcResultMatchers.view().name("courses/edit"));
    }

    @Test
    void edit_ShouldRestrictAccessIfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/1/edit")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }

    @Test
    void update_ShouldReturnView() throws Exception {
        Course course = new Course(1, "Test", new ArrayList<>());
        doNothing().when(courseService).update(course);
        doNothing().when(courseValidator).validate(course, bindingResult);
        mockMvc.perform(MockMvcRequestBuilders.patch("/courses/1")
                        .flashAttr("course", course))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/courses"));
    }

    @Test
    void update_ShouldReturnView_WhenBindingResultHasErrors() throws Exception {
        Course course = new Course();
        doNothing().when(courseService).update(course);
        mockMvc.perform(MockMvcRequestBuilders.patch("/courses/1")
                        .flashAttr("course", course))
                .andExpect(MockMvcResultMatchers.view().name("courses/edit"));
    }

    @Test
    void delete_ShouldReturnView() throws Exception {
        doNothing().when(courseService).deleteById(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/1")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/courses"));
    }

    @Test
    void delete_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/1")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }
}