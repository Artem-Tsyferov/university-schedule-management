package ua.com.foxminded.university.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ua.com.foxminded.university.models.Student;
import ua.com.foxminded.university.utils.serializer.CustomStudentSerializer;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class GroupDTO {

    private Integer id;

    @NotEmpty(message = "Name can not be empty")
    private String name;

    @JsonSerialize(using = CustomStudentSerializer.class)
    private List<Student> students;

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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
