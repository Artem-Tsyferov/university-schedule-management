package ua.com.foxminded.university.service.impl;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.models.User;
import ua.com.foxminded.university.exception.UserNotFoundException;
import ua.com.foxminded.university.repository.UserRepository;
import ua.com.foxminded.university.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final PresenceCheckerLoggerService presenceCheckerLoggerService;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(PresenceCheckerLoggerService presenceCheckerLoggerService, UserRepository userRepository) {
        this.presenceCheckerLoggerService = presenceCheckerLoggerService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void save(User user) {
        LOGGER.debug("Saving {}", user);
        try {
            userRepository.save(user);
            LOGGER.debug("User saved successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to save User due to", hibernateException);
        }
    }

    @Override
    public User findByLogin(String login) {
        LOGGER.debug("Finding User by login = {}", login);
        Optional<User> optionalUser = Optional.empty();
        try {
            optionalUser = userRepository.findByLogin(login);
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to find User due to", hibernateException);
        }
        presenceCheckerLoggerService.logIfFound(optionalUser);
        return optionalUser.orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<User> findAllUsers() {
        LOGGER.debug("Finding all Users");
        List<User> users = new ArrayList<>();
        try {
            users = userRepository.findAll();
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to find Users due to", hibernateException);
        }
        return users;
    }

    @Override
    public boolean checkIfExists(String login) {
        LOGGER.debug("Checking if User with login = {} exists", login);
        return userRepository.existsByLogin(login);
    }

    @Override
    @Transactional
    public void update(User updatedUser) {
        LOGGER.debug("Updating {}", updatedUser);
        try {
            userRepository.save(updatedUser);
            LOGGER.debug("User updated successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to update User due to", hibernateException);
        }
    }

    @Override
    @Transactional
    public void deleteByLogin(String login) {
        LOGGER.debug("Deleting User by login = {}", login);
        try {
            userRepository.deleteByLogin(login);
            LOGGER.debug("User deleted successfully");
        } catch (HibernateException hibernateException) {
            LOGGER.error("Failed to delete User due to", hibernateException);
        }
    }
}
