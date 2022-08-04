package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ua.com.foxminded.university.models.Group;
import ua.com.foxminded.university.exception.GroupNotFoundException;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LectureRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.service.impl.GroupServiceImpl;
import ua.com.foxminded.university.service.impl.PresenceCheckerLoggerService;

import java.util.Optional;

import static org.mockito.Mockito.*;

class GroupServiceImplTest {

    private GroupRepository groupRepository;
    private StudentRepository studentRepository;
    private LectureRepository lectureRepository;
    private GroupService groupService;
    private PresenceCheckerLoggerService presenceCheckerLoggerService;

    @BeforeEach
    void setUp() {
        groupRepository = mock(GroupRepository.class);
        presenceCheckerLoggerService = mock(PresenceCheckerLoggerService.class);
        studentRepository = mock(StudentRepository.class);
        lectureRepository = mock(LectureRepository.class);
        groupService = new GroupServiceImpl(presenceCheckerLoggerService, groupRepository, studentRepository, lectureRepository);
    }

    @Test
    void save_ShouldInvokeRepoMethod() {
        Group group = new Group();
        groupService.save(group);
        verify(groupRepository, times(1)).save(group);

    }

    @Test
    void save_ShouldGiveToRepoMethodArgument() {
        Group group = new Group();
        groupService.save(group);
        ArgumentCaptor<Group> captor = ArgumentCaptor.forClass(Group.class);
        verify(groupRepository).save(captor.capture());
    }

    @Test
    void findById_ShouldInvokeRepoMethod() {
        Optional<Group> optionalGroup = Optional.of(new Group());
        when(groupRepository.findById(1)).thenReturn(optionalGroup);
        groupService.findById(1);
        verify(groupRepository, times(1)).findById(1);
    }

    @Test
    void findById_ShouldGiveToRepoMethodArgument() {
        Optional<Group> optionalGroup = Optional.of(new Group());
        when(groupRepository.findById(1)).thenReturn(optionalGroup);
        groupService.findById(1);
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(groupRepository).findById(captor.capture());
    }

    @Test
    void findById_ShouldReturnExceptionIfEntityIsNotFound() {
        Optional<Group> optionalGroup = Optional.empty();
        when(groupRepository.findById(1)).thenReturn(optionalGroup);
        Assertions.assertThrows(GroupNotFoundException.class, () -> groupService.findById(1));
    }

    @Test
    void findAllGroups_ShouldInvokeRepoMethod() {
        groupService.findAllGroups();
        verify(groupRepository, times(1)).findAll();
    }

    @Test
    void update_ShouldInvokeRepoMethod() {
        Group group = new Group();
        groupService.update(group);
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void update_ShouldGiveToDaoMethodArgument() {
        Group group = new Group();
        groupService.update(group);
        ArgumentCaptor<Group> captor = ArgumentCaptor.forClass(Group.class);
        verify(groupRepository).save(captor.capture());
    }

    @Test
    void deleteById_ShouldInvokeDaoMethod() {
        groupService.deleteById(1);
        verify(groupRepository, times(1)).deleteById(1);

    }

    @Test
    void deleteById_ShouldGiveToDaoMethodArgument() {
        groupService.deleteById(1);
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(groupRepository).deleteById(captor.capture());
    }

}