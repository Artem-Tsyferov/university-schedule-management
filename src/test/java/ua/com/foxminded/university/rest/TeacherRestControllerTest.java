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
import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.exception.TeacherNotFoundException;
import ua.com.foxminded.university.models.Teacher;
import ua.com.foxminded.university.rest.impl.TeacherRestControllerImpl;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.utils.validator.TeacherValidator;
import ua.com.foxminded.university.utils.mapper.TeacherMapper;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TeacherRestControllerImpl.class)
class TeacherRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private TeacherValidator teacherValidator;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private TeacherMapper teacherMapper;

    @Test
    void create_success() throws Exception {
        Teacher teacher = createTeacher();
        TeacherDTO teacherDTO = createTeacherDTO();

        doNothing().when(teacherValidator).validate(teacher, bindingResult);
        when(teacherService.saveWithReturnSavedEntity(teacher)).thenReturn(teacher);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(teacherMapper.convertToTeacher(teacherDTO)).thenReturn(teacher);
        when(teacherMapper.convertToTeacherDTO(teacher)).thenReturn(teacherDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacherDTO));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    void find_success() throws Exception {
        Teacher teacher = createTeacher();
        TeacherDTO teacherDTO = createTeacherDTO();

        when(teacherService.findById(1)).thenReturn(teacher);
        when(teacherMapper.convertToTeacherDTO(teacher)).thenReturn(teacherDTO);

        mockMvc.perform(get("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is("Test")));
    }

    @Test
    void find_ShouldReturnNotFoundException() throws Exception {
        when(teacherService.findById(1)).thenThrow(TeacherNotFoundException.class);

        mockMvc.perform(get("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAll_success() throws Exception {
        Teacher teacher = createTeacher();
        List<Teacher> teachers = Arrays.asList(teacher);

        TeacherDTO teacherDTO = createTeacherDTO();

        when(teacherService.findAllTeachers()).thenReturn(teachers);
        when(teacherMapper.convertToTeacherDTO(teacher)).thenReturn(teacherDTO);

        mockMvc.perform(get("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", Matchers.is(teacher.getFirstName())));
    }

    @Test
    void update_success() throws Exception {
        Teacher teacher = createTeacher();
        TeacherDTO teacherDTO = createTeacherDTO();

        when(teacherService.updateWithReturnUpdatedEntity(teacher)).thenReturn(teacher);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(teacherMapper.convertToTeacher(teacherDTO)).thenReturn(teacher);
        when(teacherMapper.convertToTeacherDTO(teacher)).thenReturn(teacherDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.patch("/api/v1/teachers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacherDTO));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    void delete_success() throws Exception {
        doNothing().when(teacherService).deleteById(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private TeacherDTO createTeacherDTO() {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(1);
        teacherDTO.setFirstName("Test");
        teacherDTO.setLastName("TestTest");
        teacherDTO.setPersonnelNumber(1);
        return teacherDTO;
    }

    private Teacher createTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setFirstName("Test");
        teacher.setLastName("TestTest");
        teacher.setPersonnelNumber(1);
        return teacher;
    }

}