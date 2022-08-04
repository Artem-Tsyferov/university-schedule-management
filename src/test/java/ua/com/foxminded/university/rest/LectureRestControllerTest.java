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
import ua.com.foxminded.university.dto.LectureDTO;
import ua.com.foxminded.university.exception.LectureNotFoundException;
import ua.com.foxminded.university.models.Lecture;
import ua.com.foxminded.university.rest.impl.LectureRestControllerImpl;
import ua.com.foxminded.university.service.LectureService;
import ua.com.foxminded.university.utils.validator.LectureValidator;
import ua.com.foxminded.university.utils.mapper.LectureMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LectureRestControllerImpl.class)
class LectureRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private LectureService lectureService;
    @MockBean
    private LectureValidator lectureValidator;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private LectureMapper lectureMapper;

    @Test
    void create_success() throws Exception {
        Lecture lecture = createLecture();
        LectureDTO lectureDTO = createLectureDTO();

        doNothing().when(lectureValidator).validate(lectureDTO, bindingResult);
        when(lectureService.saveWithReturnSavedEntity(lecture)).thenReturn(lecture);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(lectureMapper.convertToLecture(lectureDTO)).thenReturn(lecture);
        when(lectureMapper.convertToLectureDTO(lecture)).thenReturn(lectureDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/lectures")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lectureDTO));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    void find_success() throws Exception {
        Lecture lecture = createLecture();
        LectureDTO lectureDTO = createLectureDTO();

        when(lectureService.findById(1)).thenReturn(lecture);
        when(lectureMapper.convertToLectureDTO(lecture)).thenReturn(lectureDTO);

        mockMvc.perform(get("/api/v1/lectures/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roomNumber", is(1)));
    }

    @Test
    void find_ShouldReturnNotFoundException() throws Exception {
        when(lectureService.findById(1)).thenThrow(LectureNotFoundException.class);

        mockMvc.perform(get("/api/v1/lectures/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAll_success() throws Exception {
        Lecture lecture = createLecture();
        List<Lecture> lectures = Arrays.asList(lecture);

        LectureDTO lectureDTO = createLectureDTO();

        when(lectureService.findAllLectures()).thenReturn(lectures);
        when(lectureMapper.convertToLectureDTO(lecture)).thenReturn(lectureDTO);

        mockMvc.perform(get("/api/v1/lectures")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].roomNumber", Matchers.is(lecture.getRoomNumber())));
    }

    @Test
    void update_success() throws Exception {
        Lecture lecture = createLecture();
        LectureDTO lectureDTO = createLectureDTO();

        doNothing().when(lectureValidator).validate(lectureDTO, bindingResult);
        when(lectureService.updateWithReturnUpdatedEntity(lecture)).thenReturn(lecture);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(lectureMapper.convertToLecture(lectureDTO)).thenReturn(lecture);
        when(lectureMapper.convertToLectureDTO(lecture)).thenReturn(lectureDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.patch("/api/v1/lectures/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lectureDTO));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    void delete_success() throws Exception {
        doNothing().when(lectureService).deleteById(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/lectures/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private LectureDTO createLectureDTO() {
        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setId(1);
        lectureDTO.setDate(LocalDate.of(2022, 12, 12));
        lectureDTO.setRoomNumber(1);
        lectureDTO.setTimeOfStart(LocalTime.of(01, 00));
        lectureDTO.setTimeOfEnd(LocalTime.of(02, 00));
        return lectureDTO;
    }

    private Lecture createLecture() {
        Lecture lecture = new Lecture();
        lecture.setId(1);
        lecture.setDate(LocalDate.of(2022, 12, 12));
        lecture.setRoomNumber(1);
        lecture.setTimeOfStart(LocalTime.of(01, 00));
        lecture.setTimeOfEnd(LocalTime.of(02, 00));
        return lecture;
    }
}