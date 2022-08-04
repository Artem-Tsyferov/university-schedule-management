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
import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.exception.StudentNotFoundException;
import ua.com.foxminded.university.models.Student;
import ua.com.foxminded.university.rest.impl.StudentRestControllerImpl;
import ua.com.foxminded.university.service.StudentService;
import ua.com.foxminded.university.utils.validator.StudentValidator;
import ua.com.foxminded.university.utils.mapper.StudentMapper;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentRestControllerImpl.class)
class StudentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private StudentService studentService;
    @MockBean
    private StudentValidator studentValidator;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private StudentMapper studentMapper;

    @Test
    void create_success() throws Exception {
        Student student = createStudent();
        StudentDTO studentDTO = createStudentDTO();

        doNothing().when(studentValidator).validate(student, bindingResult);
        when(studentService.saveWithReturnSavedEntity(student)).thenReturn(student);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(studentMapper.convertToStudent(studentDTO)).thenReturn(student);
        when(studentMapper.convertToStudentDTO(student)).thenReturn(studentDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    void find_success() throws Exception {
        Student student = createStudent();
        StudentDTO studentDTO = createStudentDTO();

        when(studentService.findById(1)).thenReturn(student);
        when(studentMapper.convertToStudentDTO(student)).thenReturn(studentDTO);

        mockMvc.perform(get("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is("Test")));
    }

    @Test
    void find_ShouldReturnNotFoundException() throws Exception {
        when(studentService.findById(1)).thenThrow(StudentNotFoundException.class);

        mockMvc.perform(get("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAll_success() throws Exception {
        Student student = createStudent();
        List<Student> students = Arrays.asList(student);

        StudentDTO studentDTO = createStudentDTO();

        when(studentService.findAllStudents()).thenReturn(students);
        when(studentMapper.convertToStudentDTO(student)).thenReturn(studentDTO);

        mockMvc.perform(get("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", Matchers.is(student.getFirstName())));
    }

    @Test
    void update_success() throws Exception {
        Student student = createStudent();
        StudentDTO studentDTO = createStudentDTO();

        when(studentService.updateWithReturnUpdatedEntity(student)).thenReturn(student);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(studentMapper.convertToStudent(studentDTO)).thenReturn(student);
        when(studentMapper.convertToStudentDTO(student)).thenReturn(studentDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.patch("/api/v1/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    void delete_success() throws Exception {
        doNothing().when(studentService).deleteById(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private StudentDTO createStudentDTO() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1);
        studentDTO.setFirstName("Test");
        studentDTO.setLastName("TestTest");
        studentDTO.setPersonnelNumber(1);
        return studentDTO;
    }

    private Student createStudent() {
        Student student = new Student();
        student.setId(1);
        student.setFirstName("Test");
        student.setLastName("TestTest");
        student.setPersonnelNumber(1);
        return student;
    }
}