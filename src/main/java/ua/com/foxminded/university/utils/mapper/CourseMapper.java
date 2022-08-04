package ua.com.foxminded.university.utils.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.CourseDTO;
import ua.com.foxminded.university.models.Course;

@Component
public class CourseMapper {

    private final ModelMapper modelMapper;

    public CourseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Course convertToCourse(CourseDTO courseDTO) {
        return modelMapper.map(courseDTO, Course.class);
    }

    public CourseDTO convertToCourseDTO(Course course) {
        return modelMapper.map(course, CourseDTO.class);
    }
}
