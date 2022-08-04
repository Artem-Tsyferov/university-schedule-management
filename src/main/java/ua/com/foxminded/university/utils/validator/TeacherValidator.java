package ua.com.foxminded.university.utils.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.com.foxminded.university.exception.EntityNotCreatedOrUpdatedException;
import ua.com.foxminded.university.models.Teacher;
import ua.com.foxminded.university.repository.TeacherRepository;

@Component
public class TeacherValidator implements Validator {
    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherValidator(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Teacher.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Teacher teacher = (Teacher) target;
        if (teacherRepository.findByPersonnelNumber(teacher.getPersonnelNumber()).isPresent()) {
            errors.rejectValue("personnelNumber", "", "This personnel number is already taken");
        }
    }

    public void validate(Object target) {
        Teacher teacher = (Teacher) target;
        if (teacherRepository.findByPersonnelNumber(teacher.getPersonnelNumber()).isPresent()) {
            throw new EntityNotCreatedOrUpdatedException("This personnel number is already taken");
        }
    }
}
