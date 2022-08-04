package ua.com.foxminded.university.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityNotCreatedOrUpdatedException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseNotFoundException.class);

    public EntityNotCreatedOrUpdatedException(String msg) {
        super(msg);
        LOGGER.error(msg);
    }
}
