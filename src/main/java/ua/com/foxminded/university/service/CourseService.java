package ua.com.foxminded.university.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.models.Course;

import java.util.List;

public interface CourseService {

    void save(Course course);
    Course saveWithReturnSavedEntity (Course course);

    Course findById(int id);

    List<Course> findAllCourses();

    Page<Course> findPaginated(Pageable pageable);

    void update(Course updatedCourse);

    Course updateWithReturnUpdatedEntity(Course updatedCourse);

    void deleteById(int id);

}
