package ua.com.foxminded.university.utils.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.LectureDTO;
import ua.com.foxminded.university.models.Lecture;

@Component
public class LectureMapper {

    private final ModelMapper modelMapper;

    public LectureMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Lecture convertToLecture(LectureDTO lectureDTO) {
        return modelMapper.map(lectureDTO, Lecture.class);
    }

    public LectureDTO convertToLectureDTO(Lecture lecture) {
        return modelMapper.map(lecture, LectureDTO.class);
    }
}
