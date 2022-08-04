package ua.com.foxminded.university.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.models.Teacher;
import ua.com.foxminded.university.rest.TeacherRestController;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.utils.validator.TeacherValidator;
import ua.com.foxminded.university.utils.mapper.TeacherMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherRestControllerImpl implements TeacherRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherRestControllerImpl.class);
    private final TeacherService teacherService;
    private final TeacherValidator teacherValidator;
    private final TeacherMapper teacherMapper;

    public TeacherRestControllerImpl(TeacherService teacherService, TeacherValidator teacherValidator, TeacherMapper teacherMapper) {
        this.teacherService = teacherService;
        this.teacherValidator = teacherValidator;
        this.teacherMapper = teacherMapper;
    }

    @Override
    @PostMapping
    public ResponseEntity<TeacherDTO> create(@RequestBody @Valid TeacherDTO teacherDTO) {
        Teacher teacher = teacherMapper.convertToTeacher(teacherDTO);
        teacherValidator.validate(teacher);
        LOGGER.debug("Saving Teacher");
        Teacher savedTeacher = teacherService.saveWithReturnSavedEntity(teacher);
        return ResponseEntity.ok(teacherMapper.convertToTeacherDTO(savedTeacher));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> find(@PathVariable("id") int id) {
        LOGGER.debug("Finding Teacher with id = {}", id);
        TeacherDTO teacherDTO = teacherMapper.convertToTeacherDTO(teacherService.findById(id));
        return ResponseEntity.ok(teacherDTO);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<TeacherDTO>> findAll() {
        LOGGER.debug("Finding all Teachers");
        List<TeacherDTO> teacherDTOS = teacherService.findAllTeachers().stream()
                .map(teacherMapper::convertToTeacherDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(teacherDTOS);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<TeacherDTO> update(@RequestBody @Valid TeacherDTO teacherDTO) {
        Teacher teacher = teacherMapper.convertToTeacher(teacherDTO);
        LOGGER.debug("Updating Teacher");
        Teacher updatedTeacher = teacherService.updateWithReturnUpdatedEntity(teacher);
        return ResponseEntity.ok(teacherMapper.convertToTeacherDTO(updatedTeacher));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        LOGGER.debug("Deleting Course with id = {}", id);
        teacherService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
