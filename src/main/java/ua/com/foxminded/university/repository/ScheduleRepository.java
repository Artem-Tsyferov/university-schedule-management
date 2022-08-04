package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.models.Schedule;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    boolean existsByDate(LocalDate date);

    @Query("select schedule, lecture from Schedule schedule " +
            "left join Lecture lecture on (schedule.date=lecture.date) " +
            "where lecture.teacher.id = :teacher_id and schedule.date in (:dates) " +
            "order by schedule.date, lecture.timeOfStart")
    List<Object[]> findSchedulesForTeacher(@Param("teacher_id") int teacherId,
                                           @Param("dates") List<LocalDate> dates);

    @Query("select schedule, lecture from Schedule schedule " +
            "left join Lecture lecture on (schedule.date=lecture.date) " +
            "left join Student student on (lecture.group.id=student.group.id) " +
            "where student.id = :student_id and schedule.date in (:dates)" +
            "order by schedule.date, lecture.timeOfStart")
    List<Object[]> findSchedulesForStudent(@Param("student_id") int teacherId,
                                           @Param("dates") List<LocalDate> dates);
}
