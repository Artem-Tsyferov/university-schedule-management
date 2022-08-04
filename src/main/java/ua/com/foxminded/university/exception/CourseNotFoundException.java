package ua.com.foxminded.university.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CourseNotFoundException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseNotFoundException.class);
    private static final String MESSAGE = "Course not found";
    public CourseNotFoundException() {
        super(MESSAGE);
        LOGGER.error(MESSAGE);
    }

}
