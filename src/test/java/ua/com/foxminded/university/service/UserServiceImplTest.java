package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ua.com.foxminded.university.models.User;
import ua.com.foxminded.university.exception.UserNotFoundException;
import ua.com.foxminded.university.repository.UserRepository;
import ua.com.foxminded.university.service.impl.PresenceCheckerLoggerService;
import ua.com.foxminded.university.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserService userService;
    private PresenceCheckerLoggerService presenceCheckerLoggerService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        presenceCheckerLoggerService = mock(PresenceCheckerLoggerService.class);
        userService = new UserServiceImpl(presenceCheckerLoggerService, userRepository);
    }

    @Test
    void save_ShouldInvokeRepoMethod() {
        User user = new User();
        userService.save(user);
        verify(userRepository, times(1)).save(user);

    }

    @Test
    void save_ShouldGiveToRepoMethodArgument() {
        User user = new User();
        userService.save(user);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
    }

    @Test
    void findByLogin_ShouldInvokeRepoMethod() {
        Optional<User> optionalUser = Optional.of(new User());
        when(userRepository.findByLogin("Test")).thenReturn(optionalUser);
        userService.findByLogin("Test");
        verify(userRepository, times(1)).findByLogin("Test");
    }

    @Test
    void findByLogin_ShouldGiveToRepoMethodArgument() {
        Optional<User> optionalUser = Optional.of(new User());
        when(userRepository.findByLogin("Test")).thenReturn(optionalUser);
        userService.findByLogin("Test");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByLogin(captor.capture());
    }

    @Test
    void findByLogin_ShouldReturnExceptionIfEntityIsNotFound() {
        Optional<User> optionalUser = Optional.empty();
        when(userRepository.findByLogin("Test")).thenReturn(optionalUser);
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findByLogin("Test"));
    }

    @Test
    void findAllUsers_ShouldInvokeRepoMethod() {
        userService.findAllUsers();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void update_ShouldInvokeRepoMethod() {
        User user = new User();
        userService.update(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void update_ShouldGiveToRepoMethodArgument() {
        User user = new User();
        userService.update(user);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
    }

    @Test
    void deleteById_ShouldInvokeRepoMethod() {
        userService.deleteByLogin("Test");
        verify(userRepository, times(1)).deleteByLogin("Test");
    }
}