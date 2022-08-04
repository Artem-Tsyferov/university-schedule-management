package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ua.com.foxminded.university.models.Student;
import ua.com.foxminded.university.exception.StudentNotFoundException;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.service.impl.PresenceCheckerLoggerService;
import ua.com.foxminded.university.service.impl.StudentServiceImpl;

import java.util.Optional;

import static org.mockito.Mockito.*;

class StudentServiceImplTest {

    private StudentRepository studentRepository;
    private StudentService studentService;
    private PresenceCheckerLoggerService presenceCheckerLoggerService;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
        presenceCheckerLoggerService = mock(PresenceCheckerLoggerService.class);
        studentService = new StudentServiceImpl(presenceCheckerLoggerService, studentRepository);
    }

    @Test
    void save_ShouldInvokeRepoMethod() {
        Student student = new Student();
        studentService.save(student);
        verify(studentRepository, times(1)).save(student);

    }

    @Test
    void save_ShouldGiveToRepoMethodArgument() {
        Student student = new Student();
        studentService.save(student);
        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
    }

    @Test
    void findById_ShouldInvokeRepoMethod() {
        Optional<Student> optionalStudent = Optional.of(new Student());
        when(studentRepository.findById(1)).thenReturn(optionalStudent);
        studentService.findById(1);
        verify(studentRepository, times(1)).findById(1);
    }

    @Test
    void findById_ShouldGiveToRepoMethodArgument() {
        Optional<Student> optionalStudent = Optional.of(new Student());
        when(studentRepository.findById(1)).thenReturn(optionalStudent);
        studentService.findById(1);
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(studentRepository).findById(captor.capture());
    }

    @Test
    void findById_ShouldReturnExceptionIfEntityIsNotFound() {
        Optional<Student> optionalStudent = Optional.empty();
        when(studentRepository.findById(1)).thenReturn(optionalStudent);
        Assertions.assertThrows(StudentNotFoundException.class, () -> studentService.findById(1));
    }

    @Test
    void findAllStudents_ShouldInvokeRepoMethod() {
        studentService.findAllStudents();
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void update_ShouldInvokeRepoMethod() {
        Student student = new Student();
        studentService.update(student);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void update_ShouldGiveToRepoMethodArgument() {
        Student student = new Student();
        studentService.update(student);
        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
    }

    @Test
    void deleteById_ShouldInvokeRepoMethod() {
        studentService.deleteById(1);
        verify(studentRepository, times(1)).deleteById(1);
    }
}