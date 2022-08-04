package ua.com.foxminded.university.utils.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.com.foxminded.university.exception.EntityNotCreatedOrUpdatedException;
import ua.com.foxminded.university.models.Group;
import ua.com.foxminded.university.repository.GroupRepository;

@Component
public class GroupValidator implements Validator {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupValidator(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Group.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Group group = (Group) target;
        if (groupRepository.findByName(group.getName()).isPresent()) {
            errors.rejectValue("name", "", "This group is already exists");
        }
    }

    public void validate(Object target) {
        Group group = (Group) target;
        if (groupRepository.findByName(group.getName()).isPresent()) {
            throw new EntityNotCreatedOrUpdatedException("This group is already exists");
        }
    }
}
