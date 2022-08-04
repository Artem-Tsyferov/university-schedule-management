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
import ua.com.foxminded.university.models.Course;
import ua.com.foxminded.university.exception.CourseNotFoundException;
import ua.com.foxminded.university.models.Lecture;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.LectureRepository;
import ua.com.foxminded.university.service.CourseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final PresenceCheckerLoggerService presenceCheckerLoggerService;
    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;

    @Autowired
    public CourseServiceImpl(PresenceCheckerLoggerService presenceCheckerLoggerService, CourseRepository courseRepository, LectureRepository lectureRepository) {
        this.presenceCheckerLoggerService = presenceCheckerLoggerService;
        this.courseRepository = courseRepository;
        this.lectureRepository = lectureRepository;
    }

    @Override
    @Transactional
    public void save(Course course) {
        LOGGER.debug("Saving {}", course);
        try {
            courseRepository.save(course);
            LOGGER.debug("Course saved successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to save Course due to", hibernateException);
        }
    }

    @Override
    @Transactional
    public Course saveWithReturnSavedEntity (Course course) {
        LOGGER.debug("Saving {}", course);
        Course savedCourse = new Course();
        try {
            savedCourse = courseRepository.save(course);
            LOGGER.debug("Course saved successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to save Course due to", hibernateException);
        }
        return savedCourse;
    }

    @Override
    public Course findById(int id) {
        LOGGER.debug("Finding Course by id = {}", id);
        Optional<Course> optionalCourse = Optional.empty();
        try {
            optionalCourse = courseRepository.findById(id);
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to find Course due to", hibernateException);
        }
        presenceCheckerLoggerService.logIfFound(optionalCourse);
        return optionalCourse.orElseThrow(CourseNotFoundException::new);
    }

    @Override
    public List<Course> findAllCourses() {
        LOGGER.debug("Finding all Courses");
        List<Course> courses = new ArrayList<>();
        try {
            courses = courseRepository.findAll();
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to find Courses due to", hibernateException);
        }
        return courses;
    }

    @Override
    public Page<Course> findPaginated(Pageable pageable) {
        LOGGER.debug("Finding Paginated");

        List<Course> courses = findAllCourses();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Course> list = new ArrayList<>();

        if (courses.size() > startItem) {
            int toIndex = Math.min(startItem + pageSize, courses.size());
            list = courses.subList(startItem, toIndex);
        }
        return new PageImpl<Course>(list, PageRequest.of(currentPage, pageSize), courses.size());
    }

    @Override
    @Transactional
    public void update(Course updatedCourse) {
        LOGGER.debug("Updating {}", updatedCourse);
        try {
            courseRepository.save(updatedCourse);
            LOGGER.debug("Course updated successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to update Course due to", hibernateException);
        }
    }

    @Override
    @Transactional
    public Course updateWithReturnUpdatedEntity(Course updatedCourse) {
        LOGGER.debug("Updating {}", updatedCourse);
        Course course = new Course();
        try {
            course = courseRepository.save(updatedCourse);
            LOGGER.debug("Course updated successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to update Course due to", hibernateException);
        }
        return course;
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        LOGGER.debug("Deleting Course with id = {}", id);
        try {
            removeFromLectures(id);
            courseRepository.deleteById(id);
            LOGGER.debug("Course deleted successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to delete Course due to", hibernateException);
        }
    }

    private void removeFromLectures(int id) {
        List<Lecture> lectures = lectureRepository.findByCourseId(id);
        lectures.forEach(lecture -> lecture.setCourse(null));
    }
}
