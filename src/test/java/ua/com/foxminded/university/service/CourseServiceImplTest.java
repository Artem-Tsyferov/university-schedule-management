package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ua.com.foxminded.university.models.Course;
import ua.com.foxminded.university.exception.CourseNotFoundException;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.LectureRepository;
import ua.com.foxminded.university.service.impl.CourseServiceImpl;
import ua.com.foxminded.university.service.impl.PresenceCheckerLoggerService;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;


class CourseServiceImplTest {

    private CourseService courseService;
    private PresenceCheckerLoggerService presenceCheckerLoggerService;
    private CourseRepository courseRepository;
    private LectureRepository lectureRepository;

    @BeforeEach
    void setUp() {
        courseRepository = mock(CourseRepository.class);
        lectureRepository = mock(LectureRepository.class);
        presenceCheckerLoggerService = mock(PresenceCheckerLoggerService.class);
        courseService = new CourseServiceImpl(presenceCheckerLoggerService, courseRepository, lectureRepository);
    }

    @Test
    void save_ShouldInvokeRepoMethod() {
        Course course = new Course(1, "Test", new ArrayList<>());
        courseService.save(course);
        verify(courseRepository, times(1)).save(course);

    }

    @Test
    void save_ShouldGiveToRepoMethodArgument() {
        Course course = new Course(1, "Test", new ArrayList<>());
        courseService.save(course);
        ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(captor.capture());
    }

    @Test
    void findById_ShouldInvokeRepoMethod() {
        Optional<Course> optionalCourse = Optional.of(new Course(1, "Test", new ArrayList<>()));
        when(courseRepository.findById(1)).thenReturn(optionalCourse);
        courseService.findById(1);
        verify(courseRepository, times(1)).findById(1);
    }

    @Test
    void findById_ShouldGiveToRepoMethodArgument() {
        Optional<Course> optionalCourse = Optional.of(new Course(1, "Test", new ArrayList<>()));
        when(courseRepository.findById(1)).thenReturn(optionalCourse);
        courseService.findById(1);
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(courseRepository).findById(captor.capture());
    }

    @Test
    void findById_ShouldReturnExceptionIfEntityIsNotFound() {
        Optional<Course> optionalCourse = Optional.empty();
        when(courseRepository.findById(1)).thenReturn(optionalCourse);
        Assertions.assertThrows(CourseNotFoundException.class, () -> courseService.findById(1));
    }

    @Test
    void findAllCourses_ShouldInvokeRepoMethod() {
        courseService.findAllCourses();
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void update_ShouldInvokeRepoMethod() {
        Course course = new Course(1, "Test", new ArrayList<>());
        courseService.update(course);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void update_ShouldGiveToRepoMethodArgument() {
        Course course = new Course(1, "Test", new ArrayList<>());
        courseService.update(course);
        ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(captor.capture());
    }

    @Test
    void deleteById_ShouldInvokeRepoMethod() {
        courseService.deleteById(1);
        verify(courseRepository, times(1)).deleteById(1);

    }

    @Test
    void deleteById_ShouldGiveToDaoMethodArgument() {
        courseService.deleteById(1);
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(courseRepository).deleteById(captor.capture());
    }

}