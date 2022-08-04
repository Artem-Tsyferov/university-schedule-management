package ua.com.foxminded.university.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.models.Group;
import ua.com.foxminded.university.rest.GroupRestController;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.utils.validator.GroupValidator;
import ua.com.foxminded.university.utils.mapper.GroupMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupRestControllerImpl implements GroupRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupRestControllerImpl.class);
    private final GroupService groupService;
    private final GroupValidator groupValidator;
    private final GroupMapper groupMapper;

    public GroupRestControllerImpl(GroupService groupService, GroupValidator groupValidator, GroupMapper groupMapper) {
        this.groupService = groupService;
        this.groupValidator = groupValidator;
        this.groupMapper = groupMapper;
    }

    @Override
    @PostMapping
    public ResponseEntity<GroupDTO> create(@RequestBody @Valid GroupDTO groupDTO) {
        Group group = groupMapper.convertToGroup(groupDTO);
        groupValidator.validate(group);
        LOGGER.debug("Saving Group");
        Group savedGroup = groupService.saveWithReturnSavedEntity(group);
        return ResponseEntity.ok(groupMapper.convertToGroupDTO(savedGroup));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> find(@PathVariable("id") int id) {
        LOGGER.debug("Finding Group with id = {}", id);
        GroupDTO groupDTO = groupMapper.convertToGroupDTO(groupService.findById(id));
        return ResponseEntity.ok(groupDTO);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<GroupDTO>> findAll() {
        LOGGER.debug("Finding all Groups");
        List<GroupDTO> groupDTOS = groupService.findAllGroups().stream()
                .map(groupMapper::convertToGroupDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(groupDTOS);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<GroupDTO> update(@RequestBody @Valid GroupDTO groupDTO) {
        Group group = groupMapper.convertToGroup(groupDTO);
        LOGGER.debug("Updating Course");
        Group updatedGroup = groupService.updateWithReturnUpdatedEntity(group);
        return ResponseEntity.ok(groupMapper.convertToGroupDTO(updatedGroup));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        LOGGER.debug("Deleting Course with id = {}", id);
        groupService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

