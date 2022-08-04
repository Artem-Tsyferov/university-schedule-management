package ua.com.foxminded.university.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import ua.com.foxminded.university.dto.CourseDTO;
import ua.com.foxminded.university.exception.CourseNotFoundException;
import ua.com.foxminded.university.models.Course;
import ua.com.foxminded.university.rest.impl.CourseRestControllerImpl;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.utils.validator.CourseValidator;
import ua.com.foxminded.university.utils.mapper.CourseMapper;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CourseRestControllerImpl.class)
class CourseRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private CourseService courseService;
    @MockBean
    private CourseValidator courseValidator;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private CourseMapper courseMapper;


    @Test
    void create_success() throws Exception {
        Course course = createCourse();
        CourseDTO courseDTO = createCourseDTO();

        doNothing().when(courseValidator).validate(course, bindingResult);
        when(courseService.saveWithReturnSavedEntity(course)).thenReturn(course);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(courseMapper.convertToCourse(courseDTO)).thenReturn(course);
        when(courseMapper.convertToCourseDTO(course)).thenReturn(courseDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseDTO));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    void find_success() throws Exception {
        Course course = createCourse();
        CourseDTO courseDTO = createCourseDTO();

        when(courseService.findById(1)).thenReturn(course);
        when(courseMapper.convertToCourseDTO(course)).thenReturn(courseDTO);

        mockMvc.perform(get("/api/v1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Test")));
    }

    @Test
    void find_ShouldReturnNotFoundException() throws Exception {
        when(courseService.findById(1)).thenThrow(CourseNotFoundException.class);

        mockMvc.perform(get("/api/v1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAll_success() throws Exception {
        Course course = createCourse();
        List<Course> courses = Arrays.asList(course);

        CourseDTO courseDTO = createCourseDTO();

        when(courseService.findAllCourses()).thenReturn(courses);
        when(courseMapper.convertToCourseDTO(course)).thenReturn(courseDTO);

        mockMvc.perform(get("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is(course.getName())));
    }

    @Test
    void update_success() throws Exception {
        Course course = createCourse();
        CourseDTO courseDTO = createCourseDTO();

        when(courseService.updateWithReturnUpdatedEntity(course)).thenReturn(course);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(courseMapper.convertToCourse(courseDTO)).thenReturn(course);
        when(courseMapper.convertToCourseDTO(course)).thenReturn(courseDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.patch("/api/v1/courses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseDTO));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    void delete_success() throws Exception {
        doNothing().when(courseService).deleteById(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private CourseDTO createCourseDTO() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(1);
        courseDTO.setName("Test");
        return courseDTO;
    }

    private Course createCourse() {
        Course course = new Course();
        course.setId(1);
        course.setName("Test");
        return course;
    }
}