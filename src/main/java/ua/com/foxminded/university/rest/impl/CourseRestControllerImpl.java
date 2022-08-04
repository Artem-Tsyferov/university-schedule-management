package ua.com.foxminded.university.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.dto.CourseDTO;
import ua.com.foxminded.university.models.Course;
import ua.com.foxminded.university.rest.CourseRestController;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.utils.validator.CourseValidator;
import ua.com.foxminded.university.utils.mapper.CourseMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseRestControllerImpl implements CourseRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseRestControllerImpl.class);
    private final CourseService courseService;
    private final CourseValidator courseValidator;

    private final CourseMapper courseMapper;

    public CourseRestControllerImpl(CourseService courseService, CourseValidator courseValidator, CourseMapper courseMapper) {
        this.courseService = courseService;
        this.courseValidator = courseValidator;
        this.courseMapper = courseMapper;
    }

    @PostMapping
    public ResponseEntity<CourseDTO> create(@RequestBody @Valid CourseDTO courseDTO) {
        Course course = courseMapper.convertToCourse(courseDTO);
        courseValidator.validate(course);
        LOGGER.debug("Saving Course");
        Course savedCourse = courseService.saveWithReturnSavedEntity(course);
        return ResponseEntity.ok(courseMapper.convertToCourseDTO(savedCourse));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> find(@PathVariable("id") int id) {
        LOGGER.debug("Finding Course with id = {}", id);
        CourseDTO courseDTO = courseMapper.convertToCourseDTO(courseService.findById(id));
        return ResponseEntity.ok(courseDTO);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<CourseDTO>> findAll() {
        LOGGER.debug("Finding all Courses");
        List<CourseDTO> coursesDTOS = courseService.findAllCourses().stream()
                .map(courseMapper::convertToCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(coursesDTOS);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<CourseDTO> update(@RequestBody @Valid CourseDTO courseDTO) {
        Course course = courseMapper.convertToCourse(courseDTO);
        LOGGER.debug("Updating Course");
        Course updatedCourse = courseService.updateWithReturnUpdatedEntity(course);
        return ResponseEntity.ok(courseMapper.convertToCourseDTO(updatedCourse));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        LOGGER.debug("Deleting Course with id = {}", id);
        courseService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
