package ua.com.foxminded.university.service;

import ua.com.foxminded.university.models.Lecture;

import java.util.List;

public interface LectureService {

    void save(Lecture lecture);

    Lecture saveWithReturnSavedEntity(Lecture lecture);

    Lecture findById(int id);

    List<Lecture> findLecturesByIds(List<Integer> lecturesId);

    List<Lecture> findAllLectures();

    void update(Lecture updatedLecture);

    Lecture updateWithReturnUpdatedEntity(Lecture updatedLecture);

    void deleteById(int id);

}
