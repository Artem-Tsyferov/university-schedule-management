package ua.com.foxminded.university.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ua.com.foxminded.university.models.Teacher;
import ua.com.foxminded.university.utils.serializer.CustomTeacherSerializer;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CourseDTO {

    private Integer id;

    @NotEmpty(message = "Name can not be empty")
    private String name;

    @JsonSerialize(using = CustomTeacherSerializer.class)
    private List<Teacher> teachers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
}
