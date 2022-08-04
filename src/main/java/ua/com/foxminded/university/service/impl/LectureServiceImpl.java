package ua.com.foxminded.university.service.impl;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.models.Lecture;
import ua.com.foxminded.university.exception.LectureNotFoundException;
import ua.com.foxminded.university.models.Schedule;
import ua.com.foxminded.university.repository.LectureRepository;
import ua.com.foxminded.university.repository.ScheduleRepository;
import ua.com.foxminded.university.service.LectureService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LectureServiceImpl implements LectureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LectureServiceImpl.class);
    private final PresenceCheckerLoggerService presenceCheckerLoggerService;
    private final LectureRepository lectureRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public LectureServiceImpl(PresenceCheckerLoggerService presenceCheckerLoggerService, LectureRepository lectureRepository, ScheduleRepository scheduleRepository) {
        this.presenceCheckerLoggerService = presenceCheckerLoggerService;
        this.lectureRepository = lectureRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    @Transactional
    public void save(Lecture lecture) {
        LOGGER.debug("Saving {}", lecture);
        try {
            setDateInSchedule(lecture);
            lectureRepository.save(lecture);
            LOGGER.debug("Lecture saved successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to save Lecture due to", hibernateException);
        }
    }

    @Override
    @Transactional
    public Lecture saveWithReturnSavedEntity(Lecture lecture) {
        LOGGER.debug("Saving {}", lecture);
        Lecture savedLecture = new Lecture();
        try {
            setDateInSchedule(lecture);
            savedLecture = lectureRepository.save(lecture);
            LOGGER.debug("Lecture saved successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to save Lecture due to", hibernateException);
        }
        return savedLecture;
    }

    @Override
    public Lecture findById(int id) {
        LOGGER.debug("Finding Lecture by id = {}", id);
        Optional<Lecture> optionalLecture = Optional.empty();
        try {
            optionalLecture = lectureRepository.findById(id);
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to find Lecture due to", hibernateException);
        }
        presenceCheckerLoggerService.logIfFound(optionalLecture);
        return optionalLecture.orElseThrow(LectureNotFoundException::new);
    }

    @Override
    public List<Lecture> findLecturesByIds(List<Integer> lecturesId) {
        LOGGER.debug("Finding Lectures by ids = {}", lecturesId);
        return lectureRepository.findByIds(lecturesId);
    }

    @Override
    public List<Lecture> findAllLectures() {
        LOGGER.debug("Finding all Lectures");
        List<Lecture> lectures = new ArrayList<>();
        try {
            lectures = lectureRepository.findAll();
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to find Lectures due to", hibernateException);
        }
        return lectures;
    }

    @Override
    @Transactional
    public void update(Lecture updatedLecture) {
        LOGGER.debug("Updating {}", updatedLecture);
        try {
            lectureRepository.save(updatedLecture);
            LOGGER.debug("Lecture updated successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to update Lecture due to", hibernateException);
        }
    }

    @Override
    @Transactional
    public Lecture updateWithReturnUpdatedEntity(Lecture updatedLecture) {
        LOGGER.debug("Updating {}", updatedLecture);
        Lecture lecture = new Lecture();
        try {
            lecture = lectureRepository.save(updatedLecture);
            LOGGER.debug("Lecture updated successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to update Lecture due to", hibernateException);
        }
        return lecture;
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        LOGGER.debug("Deleting Lecture by id = {}", id);
        try {
            lectureRepository.deleteById(id);
            LOGGER.debug("Lecture deleted successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to delete Lecture due to", hibernateException);
        }
    }

    private void setDateInSchedule(Lecture lecture) {
        if(!scheduleRepository.existsByDate(lecture.getDate())){
            Schedule schedule = new Schedule();
            schedule.setDate(lecture.getDate());
            scheduleRepository.save(schedule);
        }
    }
}
