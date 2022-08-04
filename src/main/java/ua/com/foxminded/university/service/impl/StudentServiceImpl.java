package ua.com.foxminded.university.service.impl;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.models.Student;
import ua.com.foxminded.university.exception.StudentNotFoundException;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final PresenceCheckerLoggerService presenceCheckerLoggerService;
    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(PresenceCheckerLoggerService presenceCheckerLoggerService, StudentRepository studentRepository) {
        this.presenceCheckerLoggerService = presenceCheckerLoggerService;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    public void save(Student student) {
        LOGGER.debug("Saving {}", student);
        try {
            studentRepository.save(student);
            LOGGER.debug("Student saved successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to save Student due to", hibernateException);
        }
    }

    @Override
    @Transactional
    public Student saveWithReturnSavedEntity(Student student) {
        LOGGER.debug("Saving {}", student);
        Student savedStudent = new Student();
        try {
            savedStudent = studentRepository.save(student);
            LOGGER.debug("Student saved successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to save Student due to", hibernateException);
        }
        return savedStudent;
    }

    @Override
    public Student findById(int id) {
        LOGGER.debug("Finding Student by id = {}", id);
        Optional<Student> optionalStudent = Optional.empty();
        try {
            optionalStudent = studentRepository.findById(id);
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to find Course due to", hibernateException);
        }
        presenceCheckerLoggerService.logIfFound(optionalStudent);
        return optionalStudent.orElseThrow(StudentNotFoundException::new);
    }

    @Override
    public List<Student> findAllStudents() {
        LOGGER.debug("Finding all Students");
        List<Student> students = new ArrayList<>();
        try {
            students = studentRepository.findAll();
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to find Students due to", hibernateException);
        }
        return students;
    }

    @Override
    public Page<Student> findPaginated(Pageable pageable) {
        LOGGER.debug("Finding Paginated");

        List<Student> students = findAllStudents();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Student> list = new ArrayList<>();

        if (students.size() > startItem) {
            int toIndex = Math.min(startItem + pageSize, students.size());
            list = students.subList(startItem, toIndex);
        }
        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), students.size());
    }

    @Override
    @Transactional
    public void update(Student updatedStudent) {
        LOGGER.debug("Updating {}", updatedStudent);
        try {
            studentRepository.save(updatedStudent);
            LOGGER.debug("Student updated successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to update Student due to", hibernateException);
        }
    }

    @Override
    @Transactional
    public Student updateWithReturnUpdatedEntity(Student updatedStudent) {
        LOGGER.debug("Updating {}", updatedStudent);
        Student student = new Student();
        try {
            student = studentRepository.save(updatedStudent);
            LOGGER.debug("Student updated successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to update Student due to", hibernateException);
        }
        return student;
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        LOGGER.debug("Deleting Student with id = {}", id);
        try {
            studentRepository.deleteById(id);
            LOGGER.debug("Student deleted successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to delete Student due to", hibernateException);
        }
    }
}
