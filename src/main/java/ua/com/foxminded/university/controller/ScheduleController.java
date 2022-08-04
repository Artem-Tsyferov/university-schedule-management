package ua.com.foxminded.university.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.service.ScheduleService;

import java.time.LocalDate;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleController.class);
    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/students/{id}")
    public String showScheduleForStudent(Model model,
                                         @PathVariable("id") int id) {
        LocalDate fromDate = LocalDate.now();
        LOGGER.debug("Searching schedule for student with id = {}", id);
        model.addAttribute("schedules", scheduleService.findForMonthForStudent(fromDate, id));
        return "schedules/main";
    }

    @GetMapping("/teachers/{id}")
    public String showScheduleForTeacher(Model model,
                                         @PathVariable("id") int id) {
        LocalDate fromDate = LocalDate.now();
        LOGGER.debug("Searching schedule for student with id = {}", id);
        model.addAttribute("schedules", scheduleService.findForMonthForTeacher(fromDate, id));
        return "schedules/main";
    }
}
