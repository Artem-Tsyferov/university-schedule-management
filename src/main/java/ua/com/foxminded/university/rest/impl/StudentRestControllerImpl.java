package ua.com.foxminded.university.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.models.Student;
import ua.com.foxminded.university.rest.StudentRestController;
import ua.com.foxminded.university.service.StudentService;
import ua.com.foxminded.university.utils.validator.StudentValidator;
import ua.com.foxminded.university.utils.mapper.StudentMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/students")
public class StudentRestControllerImpl implements StudentRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentRestControllerImpl.class);
    private final StudentService studentService;
    private final StudentValidator studentValidator;
    private final StudentMapper studentMapper;

    public StudentRestControllerImpl(StudentService studentService, StudentValidator studentValidator, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.studentValidator = studentValidator;
        this.studentMapper = studentMapper;
    }

    @Override
    @PostMapping
    public ResponseEntity<StudentDTO> create(@RequestBody @Valid StudentDTO studentDTO) {
        Student student = studentMapper.convertToStudent(studentDTO);
        studentValidator.validate(student);
        LOGGER.debug("Saving Student");
        Student savedStudent = studentService.saveWithReturnSavedEntity(student);
        return ResponseEntity.ok(studentMapper.convertToStudentDTO(savedStudent));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> find(@PathVariable("id") int id) {
        LOGGER.debug("Finding Student with id = {}", id);
        StudentDTO studentDTO = studentMapper.convertToStudentDTO(studentService.findById(id));
        return ResponseEntity.ok(studentDTO);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<StudentDTO>> findAll() {
        LOGGER.debug("Finding all Students");
        List<StudentDTO> studentDTOS = studentService.findAllStudents().stream()
                .map(studentMapper::convertToStudentDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentDTOS);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<StudentDTO> update(@RequestBody @Valid StudentDTO studentDTO) {
        Student student = studentMapper.convertToStudent(studentDTO);
        LOGGER.debug("Updating Course");
        Student updatedStudent = studentService.updateWithReturnUpdatedEntity(student);
        return ResponseEntity.ok(studentMapper.convertToStudentDTO(updatedStudent));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        LOGGER.debug("Deleting Course with id = {}", id);
        studentService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
