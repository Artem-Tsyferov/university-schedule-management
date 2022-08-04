package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ua.com.foxminded.university.models.Teacher;
import ua.com.foxminded.university.exception.TeacherNotFoundException;
import ua.com.foxminded.university.repository.LectureRepository;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.service.impl.PresenceCheckerLoggerService;
import ua.com.foxminded.university.service.impl.TeacherServiceImpl;

import java.util.Optional;

import static org.mockito.Mockito.*;

class TeacherServiceImplTest {

    private TeacherRepository teacherRepository;
    private PresenceCheckerLoggerService presenceCheckerLoggerService;
    private TeacherService teacherService;
    private LectureRepository lectureRepository;

    @BeforeEach
    void setUp() {
        teacherRepository = mock(TeacherRepository.class);
        presenceCheckerLoggerService = mock(PresenceCheckerLoggerService.class);
        lectureRepository = mock(LectureRepository.class);
        teacherService = new TeacherServiceImpl(presenceCheckerLoggerService, teacherRepository, lectureRepository);
    }

    @Test
    void save_ShouldInvokeRepoMethod() {
        Teacher teacher = new Teacher();
        teacherService.save(teacher);
        verify(teacherRepository, times(1)).save(teacher);

    }

    @Test
    void save_ShouldGiveToRepoMethodArgument() {
        Teacher teacher = new Teacher();
        teacherService.save(teacher);
        ArgumentCaptor<Teacher> captor = ArgumentCaptor.forClass(Teacher.class);
        verify(teacherRepository).save(captor.capture());
    }

    @Test
    void findById_ShouldInvokeRepoMethod() {
        Optional<Teacher> optionalTeacher = Optional.of(new Teacher());
        when(teacherRepository.findById(1)).thenReturn(optionalTeacher);
        teacherService.findById(1);
        verify(teacherRepository, times(1)).findById(1);
    }

    @Test
    void findById_ShouldGiveToRepoMethodArgument() {
        Optional<Teacher> optionalTeacher = Optional.of(new Teacher());
        when(teacherRepository.findById(1)).thenReturn(optionalTeacher);
        teacherService.findById(1);
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(teacherRepository).findById(captor.capture());
    }

    @Test
    void findById_ShouldReturnExceptionIfEntityIsNotFound() {
        Optional<Teacher> optionalTeacher = Optional.empty();
        when(teacherRepository.findById(1)).thenReturn(optionalTeacher);
        Assertions.assertThrows(TeacherNotFoundException.class, () -> teacherService.findById(1));
    }

    @Test
    void findAllTeachers_ShouldInvokeRepoMethod() {
        teacherService.findAllTeachers();
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void update_ShouldInvokeRepoMethod() {
        Teacher teacher = new Teacher();
        teacherService.update(teacher);
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void update_ShouldGiveToRepoMethodArgument() {
        Teacher teacher = new Teacher();
        teacherService.update(teacher);
        ArgumentCaptor<Teacher> captor = ArgumentCaptor.forClass(Teacher.class);
        verify(teacherRepository).save(captor.capture());
    }

    @Test
    void deleteById_ShouldInvokeRepoMethod() {
        teacherService.deleteById(1);
        verify(teacherRepository, times(1)).deleteById(1);
    }
}