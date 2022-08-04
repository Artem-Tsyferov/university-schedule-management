package ua.com.foxminded.university.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeacherNotFoundException extends RuntimeException{

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherNotFoundException.class);
    private static final String MESSAGE = "Teacher not found";

    public TeacherNotFoundException() {
        super(MESSAGE);
        LOGGER.error(MESSAGE);
    }

}

