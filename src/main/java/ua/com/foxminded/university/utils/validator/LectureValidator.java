package ua.com.foxminded.university.utils.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.com.foxminded.university.dto.LectureDTO;
import ua.com.foxminded.university.exception.EntityNotCreatedOrUpdatedException;
import ua.com.foxminded.university.models.Lecture;
import ua.com.foxminded.university.repository.LectureRepository;

import java.util.List;

@Component
public class LectureValidator implements Validator {
    private final LectureRepository lectureRepository;

    @Autowired
    public LectureValidator(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return LectureDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LectureDTO lectureDTO = (LectureDTO) target;
        if (lectureDTO.getTimeOfStart().compareTo(lectureDTO.getTimeOfEnd()) >= 0) {
            errors.rejectValue("timeOfEnd", "", "Wrong input of Lecture's time");
        }

        List<Lecture> lectures = lectureRepository.findByRoomNumberAndDate(lectureDTO.getRoomNumber(), lectureDTO.getDate());
        for (Lecture lecture : lectures) {
            if (lectureDTO.getTimeOfStart().compareTo(lecture.getTimeOfStart()) >= 0
                    && lectureDTO.getTimeOfStart().compareTo(lecture.getTimeOfEnd()) <= 0) {
                errors.rejectValue("timeOfEnd", "",
                        "Room for this time is already taken");
            }
        }
    }

    public void validate(Object target) {
        LectureDTO lectureDTO = (LectureDTO) target;
        if (lectureDTO.getTimeOfStart().compareTo(lectureDTO.getTimeOfEnd()) >= 0) {
            throw new EntityNotCreatedOrUpdatedException("Wrong input of Lecture's time");
        }

        List<Lecture> lectures = lectureRepository.findByRoomNumberAndDate(lectureDTO.getRoomNumber(), lectureDTO.getDate());
        for (Lecture lecture : lectures) {
            if (lectureDTO.getTimeOfStart().compareTo(lecture.getTimeOfStart()) >= 0
                    && lectureDTO.getTimeOfStart().compareTo(lecture.getTimeOfEnd()) <= 0) {
               throw new EntityNotCreatedOrUpdatedException("Room for this time is already taken");
            }
        }
    }
}
