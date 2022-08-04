package ua.com.foxminded.university.utils.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.com.foxminded.university.dto.CourseDTO;
import ua.com.foxminded.university.exception.EntityNotCreatedOrUpdatedException;
import ua.com.foxminded.university.models.Course;
import ua.com.foxminded.university.repository.CourseRepository;

@Component
public class CourseValidator implements Validator {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseValidator(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Course.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Course course = (Course) target;
        if (courseRepository.findByName(course.getName()).isPresent()) {
            errors.rejectValue("name", "", "This Course is already exists");
        }
    }

    public void validate(Object target) {
        Course course = (Course) target;
        if (courseRepository.findByName(course.getName()).isPresent()) {
            throw new EntityNotCreatedOrUpdatedException("This Course is already exists");
        }
    }

}
