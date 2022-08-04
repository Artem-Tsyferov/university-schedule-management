package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ua.com.foxminded.university.models.Lecture;
import ua.com.foxminded.university.exception.LectureNotFoundException;
import ua.com.foxminded.university.repository.LectureRepository;
import ua.com.foxminded.university.repository.ScheduleRepository;
import ua.com.foxminded.university.service.impl.LectureServiceImpl;
import ua.com.foxminded.university.service.impl.PresenceCheckerLoggerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class LectureServiceImplTest {

    private LectureService lectureService;
    private LectureRepository lectureRepository;
    private ScheduleRepository scheduleRepository;
    private PresenceCheckerLoggerService presenceCheckerLoggerService;


    @BeforeEach
    void setUp() {
        lectureRepository = mock(LectureRepository.class);
        presenceCheckerLoggerService = mock(PresenceCheckerLoggerService.class);
        scheduleRepository = mock(ScheduleRepository.class);
        lectureService = new LectureServiceImpl(presenceCheckerLoggerService, lectureRepository, scheduleRepository);
    }

    @Test
    void save_ShouldInvokeRepoMethod() {
        Lecture lecture = new Lecture();
        lectureService.save(lecture);
        verify(lectureRepository, times(1)).save(lecture);

    }

    @Test
    void save_ShouldGiveToRepoMethodArgument() {
        Lecture lecture = new Lecture();
        lectureService.save(lecture);
        ArgumentCaptor<Lecture> captor = ArgumentCaptor.forClass(Lecture.class);
        verify(lectureRepository).save(captor.capture());
    }

    @Test
    void findById_ShouldInvokeRepoMethod() {
        Optional<Lecture> optionalLecture = Optional.of(new Lecture());
        when(lectureRepository.findById(1)).thenReturn(optionalLecture);
        lectureService.findById(1);
        verify(lectureRepository, times(1)).findById(1);
    }

    @Test
    void findById_ShouldGiveToRepoMethodArgument() {
        Optional<Lecture> optionalLecture = Optional.of(new Lecture());
        when(lectureRepository.findById(1)).thenReturn(optionalLecture);
        lectureService.findById(1);
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(lectureRepository).findById(captor.capture());
    }

    @Test
    void findById_ShouldReturnExceptionIfEntityIsNotFound() {
        Optional<Lecture> optionalLecture = Optional.empty();
        when(lectureRepository.findById(1)).thenReturn(optionalLecture);
        Assertions.assertThrows(LectureNotFoundException.class, () -> lectureService.findById(1));
    }

    @Test
    void findLecturesById_ShouldInvokeRepoMethod() {
        List<Integer> lecturesIds = new ArrayList<>();
        lectureService.findLecturesByIds(lecturesIds);
        verify(lectureRepository, times(1)).findByIds(lecturesIds);
    }

    @Test
    void findAllLectures_ShouldInvokeRepoMethod() {
        lectureService.findAllLectures();
        verify(lectureRepository, times(1)).findAll();
    }

    @Test
    void update_ShouldInvokeRepoMethod() {
        Lecture lecture = new Lecture();
        lectureService.update(lecture);
        verify(lectureRepository, times(1)).save(lecture);
    }

    @Test
    void update_ShouldGiveToRepoMethodArgument() {
        Lecture lecture = new Lecture();
        lectureService.update(lecture);
        ArgumentCaptor<Lecture> captor = ArgumentCaptor.forClass(Lecture.class);
        verify(lectureRepository).save(captor.capture());
    }

    @Test
    void deleteById_ShouldInvokeRepoMethod() {
        lectureService.deleteById(1);
        verify(lectureRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteById_ShouldGiveToRepoMethodArgument() {
        lectureService.deleteById(1);
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(lectureRepository).deleteById(captor.capture());
    }


}