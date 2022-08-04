package ua.com.foxminded.university.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class LectureDTO {

    private Integer id;
    private Integer courseId;
    private Integer teacherId;
    private Integer groupId;
    @NotNull(message = "Room number can not be empty")
    @Min(value = 1, message = "Room number should be greater than 0")
    private Integer roomNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "Date must be future or present")
    @NotNull(message = "Date can not be empty")
    private LocalDate date;
    @NotNull(message = "Time can not be empty")
    private LocalTime timeOfStart;
    @NotNull(message = "Time can not be empty")
    private LocalTime timeOfEnd;

    public LectureDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTimeOfStart() {
        return timeOfStart;
    }

    public void setTimeOfStart(LocalTime timeOfStart) {
        this.timeOfStart = timeOfStart;
    }

    public LocalTime getTimeOfEnd() {
        return timeOfEnd;
    }

    public void setTimeOfEnd(LocalTime timeOfEnd) {
        this.timeOfEnd = timeOfEnd;
    }
}
