package ua.com.foxminded.university.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.exception.GroupNotFoundException;
import ua.com.foxminded.university.models.Group;
import ua.com.foxminded.university.rest.impl.GroupRestControllerImpl;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.utils.validator.GroupValidator;
import ua.com.foxminded.university.utils.mapper.GroupMapper;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GroupRestControllerImpl.class)
class GroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private GroupService groupService;
    @MockBean
    private GroupValidator groupValidator;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private GroupMapper groupMapper;

    @Test
    void create_success() throws Exception {
        Group group = createGroup();
        GroupDTO groupDTO = createGroupDTO();

        doNothing().when(groupValidator).validate(group, bindingResult);
        when(groupService.saveWithReturnSavedEntity(group)).thenReturn(group);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(groupMapper.convertToGroup(groupDTO)).thenReturn(group);
        when(groupMapper.convertToGroupDTO(group)).thenReturn(groupDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupDTO));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    void find_success() throws Exception {
        Group group = createGroup();
        GroupDTO groupDTO = createGroupDTO();

        when(groupService.findById(1)).thenReturn(group);
        when(groupMapper.convertToGroupDTO(group)).thenReturn(groupDTO);

        mockMvc.perform(get("/api/v1/groups/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Test")));
    }

    @Test
    void find_ShouldReturnNotFoundException() throws Exception {
        when(groupService.findById(1)).thenThrow(GroupNotFoundException.class);

        mockMvc.perform(get("/api/v1/groups/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAll_success() throws Exception {
        Group group = createGroup();
        List<Group> groups = Arrays.asList(group);

        GroupDTO groupDTO = createGroupDTO();

        when(groupService.findAllGroups()).thenReturn(groups);
        when(groupMapper.convertToGroupDTO(group)).thenReturn(groupDTO);

        mockMvc.perform(get("/api/v1/groups")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is(group.getName())));
    }

    @Test
    void update_success() throws Exception {
        Group group = createGroup();
        GroupDTO groupDTO = createGroupDTO();

        when(groupService.updateWithReturnUpdatedEntity(group)).thenReturn(group);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(groupMapper.convertToGroup(groupDTO)).thenReturn(group);
        when(groupMapper.convertToGroupDTO(group)).thenReturn(groupDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.patch("/api/v1/groups/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupDTO));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    void delete_success() throws Exception {
        doNothing().when(groupService).deleteById(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/groups/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private GroupDTO createGroupDTO() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(1);
        groupDTO.setName("Test");
        return groupDTO;
    }

    private Group createGroup() {
        Group group = new Group();
        group.setId(1);
        group.setName("Test");
        return group;
    }
}