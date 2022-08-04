package ua.com.foxminded.university.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.models.Student;

import java.util.List;

public interface StudentService {

    void save(Student student);

    Student saveWithReturnSavedEntity(Student student);

    Student findById(int id);

    List<Student> findAllStudents();

    Page<Student> findPaginated(Pageable pageable);

    void update(Student updatedStudent);

    Student updateWithReturnUpdatedEntity(Student updatedStudent);

    void deleteById(int id);
}
