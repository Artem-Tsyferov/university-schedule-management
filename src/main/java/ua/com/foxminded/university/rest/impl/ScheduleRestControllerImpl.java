package ua.com.foxminded.university.rest.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.foxminded.university.dto.ScheduleDTO;
import ua.com.foxminded.university.rest.ScheduleRestController;
import ua.com.foxminded.university.service.ScheduleService;
import ua.com.foxminded.university.utils.mapper.ScheduleMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/schedules")
public class ScheduleRestControllerImpl implements ScheduleRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleRestControllerImpl.class);
    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;

    public ScheduleRestControllerImpl(ScheduleService scheduleService, ScheduleMapper scheduleMapper) {
        this.scheduleService = scheduleService;
        this.scheduleMapper = scheduleMapper;
    }

    @Override
    @GetMapping("/students/{id}")
    public ResponseEntity<List<ScheduleDTO>> findForMonthForStudent(@PathVariable("id") int id) {
        LOGGER.debug("Searching schedule for Student with id = {}", id);
        LocalDate fromDate = LocalDate.now();
        List<ScheduleDTO> scheduleDTOS = scheduleService.findForMonthForStudent(fromDate, id).stream()
                .map(scheduleMapper::convertToScheduleDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(scheduleDTOS);
    }

    @Override
    @GetMapping("/teachers/{id}")
    public ResponseEntity<List<ScheduleDTO>> findForMonthForTeacher(@PathVariable("id") int id) {
        LOGGER.debug("Searching schedule for Teacher with id = {}", id);
        LocalDate fromDate = LocalDate.now();
        List<ScheduleDTO> scheduleDTOS = scheduleService.findForMonthForTeacher(fromDate, id).stream()
                .map(scheduleMapper::convertToScheduleDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(scheduleDTOS);
    }
}
