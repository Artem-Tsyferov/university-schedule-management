package ua.com.foxminded.university.service;

import ua.com.foxminded.university.models.User;

import java.util.List;

public interface UserService {

    void save(User user);

    User findByLogin(String login);

    List<User> findAllUsers();

    boolean checkIfExists(String login);

    void update(User updatedUser);

    void deleteByLogin(String login);
}
