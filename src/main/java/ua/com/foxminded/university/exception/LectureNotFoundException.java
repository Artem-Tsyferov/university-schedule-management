package ua.com.foxminded.university.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LectureNotFoundException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(LectureNotFoundException.class);
    private static final String MESSAGE = "Lecture not found";

    public LectureNotFoundException() {
        super(MESSAGE);
        LOGGER.error(MESSAGE);
    }
}
