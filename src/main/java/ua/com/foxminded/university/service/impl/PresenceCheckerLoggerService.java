package ua.com.foxminded.university.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PresenceCheckerLoggerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PresenceCheckerLoggerService.class);

    public <T> void logIfFound(Optional<T> optionalEntity) {
        optionalEntity.ifPresent(optional -> LOGGER.debug("{} found", optional.getClass().getSimpleName()));
    }
}
