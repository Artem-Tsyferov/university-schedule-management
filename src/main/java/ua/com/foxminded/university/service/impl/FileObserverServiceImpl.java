package ua.com.foxminded.university.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.service.FileObserverService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileObserverServiceImpl implements FileObserverService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileObserverServiceImpl.class);

    @Override
    public String readScript(String fileName) {
        Path path = null;
        List<String> collectionFromFile = new ArrayList<>();
        try {
            path = Paths.get(this.getClass().getClassLoader().getResource(fileName).toURI());
            collectionFromFile = Files.readAllLines(path);
        } catch (URISyntaxException exception) {
            LOGGER.error("Cannot convert to a URI", exception);
        } catch (IOException exception) {
            LOGGER.error("Input operation is failed", exception);
        }
        return collectionFromFile.stream().collect(Collectors.joining());
    }
}
