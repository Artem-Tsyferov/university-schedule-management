package ua.com.foxminded.university.utils.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.com.foxminded.university.exception.EntityNotCreatedOrUpdatedException;
import ua.com.foxminded.university.models.Student;
import ua.com.foxminded.university.repository.StudentRepository;

@Component
public class StudentValidator implements Validator {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentValidator(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Student.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Student student = (Student) target;
        if (studentRepository.findByPersonnelNumber(student.getPersonnelNumber()).isPresent()) {
            errors.rejectValue("personnelNumber", "", "This personnel number is already taken");
        }
    }

    public void validate(Object target) {
        Student student = (Student) target;
        if (studentRepository.findByPersonnelNumber(student.getPersonnelNumber()).isPresent()) {
            throw new EntityNotCreatedOrUpdatedException("This personnel number is already taken");
        }
    }
}
