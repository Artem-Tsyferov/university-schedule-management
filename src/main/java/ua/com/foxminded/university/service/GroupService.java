package ua.com.foxminded.university.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.models.Group;

import java.util.List;

public interface GroupService {

    void save(Group group);

    Group saveWithReturnSavedEntity(Group group);

    Group findById(int id);

    List<Group> findAllGroups();

    Page<Group> findPaginated(Pageable pageable);

    void update(Group updatedGroup);

    Group updateWithReturnUpdatedEntity(Group updatedGroup);

    void deleteById(int id);

}
