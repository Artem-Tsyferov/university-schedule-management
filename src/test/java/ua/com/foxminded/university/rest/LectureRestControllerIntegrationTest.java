package ua.com.foxminded.university.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.foxminded.university.dto.LectureDTO;
import ua.com.foxminded.university.models.Lecture;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LectureRestControllerIntegrationTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void create_success() throws Exception {
        LectureDTO lectureDTO = createLectureDTO();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/lectures")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lectureDTO));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = {"classpath:sql/tables_filler.sql"})
    void find_success() throws Exception {
        mockMvc.perform(get("/api/v1/lectures/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roomNumber", is(330)));
    }

    @Test
    void find_ShouldReturnNotFoundException() throws Exception {
        mockMvc.perform(get("/api/v1/lectures/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = {"classpath:sql/tables_filler.sql"})
    void findAll_success() throws Exception {
        mockMvc.perform(get("/api/v1/lectures")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].roomNumber", Matchers.is(330)));
    }

    @Test
    void update_success() throws Exception {
        LectureDTO lectureDTO = createLectureDTO();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.patch("/api/v1/lectures/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lectureDTO));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = {"classpath:sql/tables_filler.sql"})
    void delete_success() throws Exception {
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