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
import ua.com.foxminded.university.models.Lecture;
import ua.com.foxminded.university.models.Teacher;
import ua.com.foxminded.university.exception.TeacherNotFoundException;
import ua.com.foxminded.university.repository.LectureRepository;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.service.TeacherService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TeacherServiceImpl implements TeacherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherServiceImpl.class);
    private final PresenceCheckerLoggerService presenceCheckerLoggerService;
    private final TeacherRepository teacherRepository;
    private final LectureRepository lectureRepository;

    @Autowired
    public TeacherServiceImpl(PresenceCheckerLoggerService presenceCheckerLoggerService, TeacherRepository teacherRepository, LectureRepository lectureRepository) {
        this.presenceCheckerLoggerService = presenceCheckerLoggerService;
        this.teacherRepository = teacherRepository;
        this.lectureRepository = lectureRepository;
    }


    @Override
    @Transactional
    public void save(Teacher teacher) {
        LOGGER.debug("Saving {}", teacher);
        try {
            teacherRepository.save(teacher);
            LOGGER.debug("Teacher saved successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to save Teacher due to", hibernateException);
        }
    }

    @Override
    @Transactional
    public Teacher saveWithReturnSavedEntity(Teacher teacher) {
        LOGGER.debug("Saving {}", teacher);
        Teacher savedTeacher = new Teacher();
        try {
            savedTeacher = teacherRepository.save(teacher);
            LOGGER.debug("Teacher saved successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to save Teacher due to", hibernateException);
        }
        return savedTeacher;
    }

    @Override
    public Teacher findById(int id) {
        LOGGER.debug("Finding Teacher by id = {}", id);
        Optional<Teacher> optionalTeacher = Optional.empty();
        try {
            optionalTeacher = teacherRepository.findById(id);
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to find Teacher due to", hibernateException);
        }
        presenceCheckerLoggerService.logIfFound(optionalTeacher);
        return optionalTeacher.orElseThrow(TeacherNotFoundException::new);
    }

    @Override
    public List<Teacher> findAllTeachers() {
        LOGGER.debug("Finding all Teachers");
        List<Teacher> teachers = new ArrayList<>();
        try {
            teachers = teacherRepository.findAll();
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to find Teachers due to", hibernateException);
        }
        return teachers;
    }

    @Override
    public Page<Teacher> findPaginated(Pageable pageable) {
        LOGGER.debug("Finding Paginated");

        List<Teacher> teachers = findAllTeachers();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Teacher> list = new ArrayList<>();

        if (teachers.size() > startItem) {
            int toIndex = Math.min(startItem + pageSize, teachers.size());
            list = teachers.subList(startItem, toIndex);
        }
        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), teachers.size());
    }

    @Override
    @Transactional
    public void update(Teacher updatedTeacher) {
        LOGGER.debug("Updating {}", updatedTeacher);
        try {
            teacherRepository.save(updatedTeacher);
            LOGGER.debug("Teacher updated successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to update Teacher due to", hibernateException);
        }
    }

    @Override
    @Transactional
    public Teacher updateWithReturnUpdatedEntity(Teacher updatedTeacher) {
        LOGGER.debug("Updating {}", updatedTeacher);
        Teacher teacher = new Teacher();
        try {
            teacher = teacherRepository.save(updatedTeacher);
            LOGGER.debug("Teacher updated successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to update Teacher due to", hibernateException);
        }
        return teacher;
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        LOGGER.debug("Deleting Teacher by id = {}", id);
        try {
            removeFromLectures(id);
            teacherRepository.deleteById(id);
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to delete Teacher due to", hibernateException);
        }
    }

    private void removeFromLectures(int id) {
        List<Lecture> lectures = lectureRepository.findByTeacherId(id);
        lectures.forEach(lecture -> lecture.setTeacher(null));
    }
}
