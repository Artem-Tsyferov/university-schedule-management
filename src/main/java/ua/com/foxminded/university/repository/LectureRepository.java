package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.models.Lecture;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    List<Lecture> findByCourseId(Integer id);

    List<Lecture> findByTeacherId(Integer id);

    List<Lecture> findByGroupId(Integer id);

    @Query("select l from Lecture l  where l.id in (:ids)")
    List<Lecture> findByIds(@Param("ids") List<Integer> ids);

    List<Lecture> findByRoomNumberAndDate(Integer roomNumber, LocalDate date);
}
