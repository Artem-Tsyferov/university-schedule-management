package ua.com.foxminded.university.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserNotFoundException extends RuntimeException{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserNotFoundException.class);
    private static final String MESSAGE = "User not found";
    public UserNotFoundException() {
        super(MESSAGE);
        LOGGER.error(MESSAGE);
    }
}
