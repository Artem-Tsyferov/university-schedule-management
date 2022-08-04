package ua.com.foxminded.university.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupNotFoundException extends RuntimeException{

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupNotFoundException.class);
    private static final String MESSAGE = "Group not found";
    public GroupNotFoundException() {
        super(MESSAGE);
        LOGGER.error(MESSAGE);
    }
}
