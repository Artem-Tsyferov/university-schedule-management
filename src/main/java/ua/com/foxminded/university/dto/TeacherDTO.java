package ua.com.foxminded.university.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ua.com.foxminded.university.models.Course;
import ua.com.foxminded.university.utils.serializer.CustomCourseSerializer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class TeacherDTO {

    private Integer id;

    @NotEmpty(message = "First name can not be empty")
    private String firstName;

    @NotEmpty(message = "Last name can not be empty")
    private String lastName;

    @Min(value = 1, message = "personnel number should be greater than 0")
    @NotNull(message = "Personnel number can not be empty")
    private Integer personnelNumber;

    @JsonSerialize(using = CustomCourseSerializer.class)
    private List<Course> courses;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(Integer personnelNumber) {
        this.personnelNumber = personnelNumber;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
