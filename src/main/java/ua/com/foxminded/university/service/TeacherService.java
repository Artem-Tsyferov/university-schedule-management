package ua.com.foxminded.university.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.models.Teacher;

import java.util.List;

public interface TeacherService {

    void save(Teacher teacher);

    Teacher saveWithReturnSavedEntity(Teacher teacher);

    Teacher findById(int id);

    List<Teacher> findAllTeachers();

    Page<Teacher> findPaginated(Pageable pageable);

    void update(Teacher updatedTeacher);

    Teacher updateWithReturnUpdatedEntity(Teacher updatedTeacher);

    void deleteById(int id);

}
