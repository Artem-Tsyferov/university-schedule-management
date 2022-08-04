package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import ua.com.foxminded.university.models.Group;
import ua.com.foxminded.university.models.enums.UserRole;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.utils.validator.GroupValidator;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GroupControllerTest {

    private MockMvc mockMvc;
    @Mock
    private GroupService groupService;
    @Mock
    private GroupValidator groupValidator;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private GroupController groupController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    void newGroup_ShouldContainModelAndReturnView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/groups/new")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.view().name("groups/new"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("group"));
    }

    @Test
    void newGroup_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/groups/new")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }

    @Test
    void create_ShouldContainModelAndReturnView() throws Exception {
        Group group = new Group();
        group.setName("aba");
        doNothing().when(groupValidator).validate(group, bindingResult);
        doNothing().when(groupService).save(group);
        mockMvc.perform(MockMvcRequestBuilders.post("/groups")
                        .flashAttr("group", group))
                .andExpect(MockMvcResultMatchers.model().attribute("group", group))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/groups"));
    }

    @Test
    void create_ShouldContainModelAndReturnView_WhenBindingResultHasErrors() throws Exception {
        Group group = new Group();
        doNothing().when(groupService).save(group);
        mockMvc.perform(MockMvcRequestBuilders.post("/groups")
                        .flashAttr("group", group))
                .andExpect(MockMvcResultMatchers.model().attribute("group", group))
                .andExpect(MockMvcResultMatchers.view().name("groups/new"));
    }

    @Test
    void show_ShouldContainModelAndReturnView() throws Exception {
        Group group = new Group();
        when(groupService.findById(1)).thenReturn(group);
        mockMvc.perform(MockMvcRequestBuilders.get("/groups/1"))
                .andExpect(MockMvcResultMatchers.model().attribute("group", group))
                .andExpect(MockMvcResultMatchers.view().name("groups/show"));
    }

    @Test
    void index_ShouldContainModelsAndReturnView() throws Exception {
        Group group = new Group();
        List<Group> groups = new ArrayList<>();
        groups.add(group);
        Page<Group> groupPage = new PageImpl<>(groups);
        when(groupService.findPaginated(PageRequest.of(0, 5))).thenReturn(groupPage);
        mockMvc.perform(MockMvcRequestBuilders.get("/groups"))
                .andExpect(MockMvcResultMatchers.view().name("groups/main"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("pageNumbers"))
                .andExpect(MockMvcResultMatchers.model().attribute("groupPage", groupPage));
    }

    @Test
    void edit_ShouldContainModelsAndReturnView() throws Exception {
        Group group = new Group();
        when(groupService.findById(1)).thenReturn(group);
        mockMvc.perform(MockMvcRequestBuilders.get("/groups/1/edit")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.model().attribute("group", group))
                .andExpect(MockMvcResultMatchers.view().name("groups/edit"));
    }

    @Test
    void edit_ShouldRestrictAccessIfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/groups/1/edit")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }

    @Test
    void update_ShouldReturnView() throws Exception {
        Group group = new Group();
        group.setName("abc");
        doNothing().when(groupValidator).validate(group, bindingResult);
        doNothing().when(groupService).update(group);
        mockMvc.perform(MockMvcRequestBuilders.patch("/groups/1")
                        .flashAttr("group", group))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/groups"));
    }

    @Test
    void update_ShouldReturnView_WhenBindingResultHasErrors() throws Exception {
        Group group = new Group();
        doNothing().when(groupService).update(group);
        mockMvc.perform(MockMvcRequestBuilders.patch("/groups/1")
                        .flashAttr("group", group))
                .andExpect(MockMvcResultMatchers.view().name("groups/edit"));
    }

    @Test
    void delete_ShouldReturnView() throws Exception {
        doNothing().when(groupService).deleteById(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/groups/1")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/groups"));
    }

    @Test
    void delete_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/groups/1")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(MockMvcResultMatchers.view().name("security/accessDenied"));
    }
}