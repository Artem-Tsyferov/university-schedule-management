package ua.com.foxminded.university.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;
import ua.com.foxminded.university.models.Lecture;
import ua.com.foxminded.university.utils.serializer.CustomLectureSerializer;

import java.time.LocalDate;
import java.util.List;

public class ScheduleDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonSerialize(using = CustomLectureSerializer.class)
    private List<Lecture> lectures;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }
}
