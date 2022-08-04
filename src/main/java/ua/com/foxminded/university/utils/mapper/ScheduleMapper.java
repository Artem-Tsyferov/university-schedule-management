package ua.com.foxminded.university.utils.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.ScheduleDTO;
import ua.com.foxminded.university.models.Schedule;

@Component
public class ScheduleMapper {

    private final ModelMapper modelMapper;

    public ScheduleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ScheduleDTO convertToScheduleDTO (Schedule schedule) {
        return modelMapper.map(schedule, ScheduleDTO.class);
    }
}
