package ua.com.foxminded.university.utils.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.models.Teacher;

@Component
public class TeacherMapper {

    private final ModelMapper modelMapper;

    public TeacherMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Teacher convertToTeacher(TeacherDTO teacherDTO) {
        return modelMapper.map(teacherDTO, Teacher.class);
    }

    public TeacherDTO convertToTeacherDTO(Teacher teacher) {
        return modelMapper.map(teacher, TeacherDTO.class);
    }
}
