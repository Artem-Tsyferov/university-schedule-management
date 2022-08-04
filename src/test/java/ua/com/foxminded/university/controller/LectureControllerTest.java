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
import org.springframework.validation.BindingResult;
import ua.com.foxminded.university.models.*;
import ua.com.foxminded.university.models.enums.UserRole;
import ua.com.foxminded.university.dto.LectureDTO;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LectureService;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.utils.validator.LectureValidator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LectureControllerTest {

    private MockMvc mockMvc;
    @Mock
    private LectureService lectureService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private GroupService groupService;
    @Mock
    private CourseService courseService;
    @Mock
    private LectureValidator lectureValidator;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private LectureController lectureController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(lectureController).build();
    }

    @Test
    void newLecture_ShouldContainModelAndReturnView() throws Exception {
        List<Teacher> teachers = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        when(teacherService.findAllTeachers()).thenReturn(teachers);
        when(groupService.findAllGroups()).thenReturn(groups);
        when(courseService.findAllCourses()).thenReturn(courses);
        mockMvc.perform(MockMvcRequestBuilders.get("/lectures/new")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.view().name("lectures/new"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lectureDTO"))
                .andExpect(MockMvcResultMatchers.model().attribute("teachers", teachers))
                .andExpect(MockMvcResultMatchers.model().attribute("groups", groups))
                .andExpect(MockMvcResultMatchers.model().attribute("courses", courses));
    }

    @Test
    void newLecture_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lectures/new")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }

    @Test
    void create_ShouldContainModelAndReturnView() throws Exception {
        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setCourseId(1);
        lectureDTO.setGroupId(1);
        lectureDTO.setTeacherId(1);
        lectureDTO.setRoomNumber(1);
        lectureDTO.setDate(LocalDate.of(3000, 01, 01));
        lectureDTO.setTimeOfStart(LocalTime.of(11, 11));
        lectureDTO.setTimeOfEnd(LocalTime.of(12, 22));
        doNothing().when(lectureValidator).validate(lectureDTO, bindingResult);
        mockMvc.perform(MockMvcRequestBuilders.post("/lectures")
                        .flashAttr("lectureDTO", lectureDTO))
                .andExpect(MockMvcResultMatchers.model().attribute("lectureDTO", lectureDTO))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"));
    }

    @Test
    void create_ShouldContainModelAndReturnView_WhenBindingResultHasErrors() throws Exception {
        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setCourseId(1);
        lectureDTO.setGroupId(1);
        lectureDTO.setTeacherId(1);
        mockMvc.perform(MockMvcRequestBuilders.post("/lectures")
                        .flashAttr("lectureDTO", lectureDTO))
                .andExpect(MockMvcResultMatchers.model().attribute("lectureDTO", lectureDTO))
                .andExpect(MockMvcResultMatchers.view().name("lectures/new"));
    }

    @Test
    void show_ShouldContainModelAndReturnView() throws Exception {
        Lecture lecture = new Lecture();
        when(lectureService.findById(1)).thenReturn(lecture);
        mockMvc.perform(MockMvcRequestBuilders.get("/lectures/1"))
                .andExpect(MockMvcResultMatchers.model().attribute("lecture", lecture))
                .andExpect(MockMvcResultMatchers.view().name("lectures/show"));
    }

    @Test
    void edit_ShouldContainModelsAndReturnView() throws Exception {
        Lecture lecture = new Lecture();
        List<Teacher> teachers = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        when(lectureService.findById(1)).thenReturn(lecture);
        when(teacherService.findAllTeachers()).thenReturn(teachers);
        when(groupService.findAllGroups()).thenReturn(groups);
        when(courseService.findAllCourses()).thenReturn(courses);
        mockMvc.perform(MockMvcRequestBuilders.get("/lectures/1/edit")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.model().attribute("lecture", lecture))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lectureDTO"))
                .andExpect(MockMvcResultMatchers.model().attribute("teachers", teachers))
                .andExpect(MockMvcResultMatchers.model().attribute("groups", groups))
                .andExpect(MockMvcResultMatchers.model().attribute("courses", courses))
                .andExpect(MockMvcResultMatchers.view().name("lectures/edit"));
    }

    @Test
    void edit_ShouldRestrictAccessIfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lectures/1/edit")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }

    @Test
    void update_ShouldReturnView() throws Exception {
        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setId(1);
        lectureDTO.setGroupId(1);
        lectureDTO.setCourseId(1);
        lectureDTO.setTeacherId(1);
        lectureDTO.setRoomNumber(1);
        lectureDTO.setDate(LocalDate.of(3000, 01, 01));
        lectureDTO.setTimeOfStart(LocalTime.of(11, 11));
        lectureDTO.setTimeOfEnd(LocalTime.of(12, 22));
        Lecture lecture = new Lecture();
        doNothing().when(lectureService).update(lecture);
        mockMvc.perform(MockMvcRequestBuilders.patch("/lectures/1")
                        .flashAttr("lectureDTO", lectureDTO)
                        .flashAttr("lecture", lecture))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"));
    }

    @Test
    void update_ShouldReturnView_WhenBindingResultHasErrors() throws Exception {
        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setId(1);
        lectureDTO.setGroupId(1);
        lectureDTO.setCourseId(1);
        lectureDTO.setTeacherId(1);
        Lecture lecture = new Lecture();
        doNothing().when(lectureService).update(lecture);
        mockMvc.perform(MockMvcRequestBuilders.patch("/lectures/1")
                        .flashAttr("lectureDTO", lectureDTO)
                        .flashAttr("lecture", lecture))
                .andExpect(MockMvcResultMatchers.view().name("lectures/edit"));
    }

    @Test
    void delete_ShouldReturnView() throws Exception {
        doNothing().when(lectureService).deleteById(1);
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