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
import ua.com.foxminded.university.models.Group;
import ua.com.foxminded.university.exception.GroupNotFoundException;
import ua.com.foxminded.university.models.Lecture;
import ua.com.foxminded.university.models.Student;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LectureRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.service.GroupService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);
    private final PresenceCheckerLoggerService presenceCheckerLoggerService;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final LectureRepository lectureRepository;

    @Autowired
    public GroupServiceImpl(PresenceCheckerLoggerService presenceCheckerLoggerService, GroupRepository groupRepository, StudentRepository studentRepository, LectureRepository lectureRepository) {
        this.presenceCheckerLoggerService = presenceCheckerLoggerService;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.lectureRepository = lectureRepository;
    }


    @Override
    @Transactional
    public void save(Group group) {
        LOGGER.debug("Saving {}", group);
        try {
            groupRepository.save(group);
            LOGGER.debug("Group saved successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to save Group due to", hibernateException);
        }
    }

    @Override
    @Transactional
    public Group saveWithReturnSavedEntity(Group group) {
        LOGGER.debug("Saving {}", group);
        Group savedGroup = new Group();
        try {
            savedGroup = groupRepository.save(group);
            LOGGER.debug("Group saved successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to save Group due to", hibernateException);
        }
        return savedGroup;
    }

    @Override
    public Group findById(int id) {
        LOGGER.debug("Finding Group by id = {}", id);
        Optional<Group> optionalGroup = Optional.empty();
        try {
            optionalGroup = groupRepository.findById(id);
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to find Course due to", hibernateException);
        }
        presenceCheckerLoggerService.logIfFound(optionalGroup);
        return optionalGroup.orElseThrow(GroupNotFoundException::new);
    }

    @Override
    public List<Group> findAllGroups() {
        LOGGER.debug("Finding all Groups");
        List<Group> groups = new ArrayList<>();
        try {
            groups = groupRepository.findAll();
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to find Groups due to", hibernateException);
        }
        return groups;
    }

    @Override
    public Page<Group> findPaginated(Pageable pageable) {
        LOGGER.debug("Finding Paginated");

        List<Group> groups = findAllGroups();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Group> list = new ArrayList<>();

        if (groups.size() > startItem) {
            int toIndex = Math.min(startItem + pageSize, groups.size());
            list = groups.subList(startItem, toIndex);
        }
        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), groups.size());
    }

    @Override
    @Transactional
    public void update(Group updatedGroup) {
        LOGGER.debug("Updating {}", updatedGroup);
        try {
            groupRepository.save(updatedGroup);
            LOGGER.debug("Group updated successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to update Group due to", hibernateException);
        }
    }

    @Override
    @Transactional
    public Group updateWithReturnUpdatedEntity(Group updatedGroup) {
        LOGGER.debug("Updating {}", updatedGroup);
        Group group = new Group();
        try {
            group = groupRepository.save(updatedGroup);
            LOGGER.debug("Group updated successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to update Group due to", hibernateException);
        }
        return group;
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        LOGGER.debug("Deleting Group with id = {}", id);
        try {
            removeFromStudents(id);
            removeFromLectures(id);
            groupRepository.deleteById(id);
            LOGGER.debug("Group deleted successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to delete Group due to", hibernateException);
        }
    }

    private void removeFromLectures(int id) {
        List<Lecture> lectures = lectureRepository.findByGroupId(id);
        lectures.forEach(lecture -> lecture.setGroup(null));
    }

    private void removeFromStudents(int id) {
        List<Student> students = studentRepository.findByGroupId(id);
        students.forEach(student -> student.setGroup(null));
    }
}
