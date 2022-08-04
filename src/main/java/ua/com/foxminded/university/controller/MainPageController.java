package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainPageController.class);

    @GetMapping("/")
    public String showMainPage() {
        LOGGER.debug("Showing main page");
        return "index";
    }

}
