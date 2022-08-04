package ua.com.foxminded.university.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentNotFoundException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentNotFoundException.class);
    private static final String MESSAGE = "Student not found";
    public StudentNotFoundException() {
        super(MESSAGE);
        LOGGER.error(MESSAGE);
    }

}
