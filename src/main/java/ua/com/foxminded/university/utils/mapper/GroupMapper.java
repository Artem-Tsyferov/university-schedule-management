package ua.com.foxminded.university.utils.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.models.Group;

@Component
public class GroupMapper {

    private final ModelMapper modelMapper;

    public GroupMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public Group convertToGroup(GroupDTO groupDTO) {
        return modelMapper.map(groupDTO, Group.class);
    }


    public GroupDTO convertToGroupDTO(Group group) {
        return modelMapper.map(group, GroupDTO.class);
    }
}
