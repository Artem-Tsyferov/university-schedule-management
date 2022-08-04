package ua.com.foxminded.university.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.dto.LectureDTO;
import ua.com.foxminded.university.models.Lecture;
import ua.com.foxminded.university.rest.LectureRestController;
import ua.com.foxminded.university.service.LectureService;
import ua.com.foxminded.university.utils.validator.LectureValidator;
import ua.com.foxminded.university.utils.mapper.LectureMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/lectures")
public class LectureRestControllerImpl implements LectureRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LectureRestControllerImpl.class);
    private final LectureService lectureService;
    private final LectureValidator lectureValidator;
    private final LectureMapper lectureMapper;

    public LectureRestControllerImpl(LectureService lectureService, LectureValidator lectureValidator, LectureMapper lectureMapper) {
        this.lectureService = lectureService;
        this.lectureValidator = lectureValidator;
        this.lectureMapper = lectureMapper;
    }

    @Override
    @PostMapping
    public ResponseEntity<LectureDTO> create(@RequestBody @Valid LectureDTO lectureDTO) {
        lectureValidator.validate(lectureDTO);
        Lecture lecture = lectureMapper.convertToLecture(lectureDTO);
        LOGGER.debug("Saving Lecture");
        Lecture savedLecture = lectureService.saveWithReturnSavedEntity(lecture);
        return ResponseEntity.ok(lectureMapper.convertToLectureDTO(savedLecture));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<LectureDTO> find(@PathVariable("id") int id) {
        LOGGER.debug("Finding Lecture with id = {}", id);
        LectureDTO lectureDTO = lectureMapper.convertToLectureDTO(lectureService.findById(id));
        return ResponseEntity.ok(lectureDTO);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<LectureDTO>> findAll() {
        LOGGER.debug("Finding all Lectures");
        List<LectureDTO> lectureDTOS = lectureService.findAllLectures().stream()
                .map(lectureMapper::convertToLectureDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lectureDTOS);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<LectureDTO> update(@RequestBody @Valid LectureDTO lectureDTO) {
        lectureValidator.validate(lectureDTO);
        Lecture lecture = lectureMapper.convertToLecture(lectureDTO);
        LOGGER.debug("Updating Lecture");
        Lecture updatedLecture = lectureService.updateWithReturnUpdatedEntity(lecture);
        return ResponseEntity.ok(lectureMapper.convertToLectureDTO(updatedLecture));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        LOGGER.debug("Deleting Course with id = {}", id);
        lectureService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
