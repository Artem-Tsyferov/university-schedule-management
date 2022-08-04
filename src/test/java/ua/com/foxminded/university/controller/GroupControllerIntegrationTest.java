package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.foxminded.university.models.Group;
import ua.com.foxminded.university.models.enums.UserRole;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GroupControllerIntegrationTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void newGroup_ShouldContainModelAndReturnView() throws Exception {
        mockMvc.perform(get("/groups/new")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(view().name("groups/new"))
                .andExpect(model().attributeExists("group"));
    }

    @Test
    void newGroup_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(get("/groups/new")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(view().name("security/accessDenied"));
    }

    @Test
    void create_ShouldContainModelAndReturnView() throws Exception {
        Group group = new Group();
        group.setName("aba");
        mockMvc.perform(post("/groups")
                        .flashAttr("group", group))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/groups"));
    }

    @Test
    void create_ShouldContainModelAndReturnView_WhenBindingResultHasErrors() throws Exception {
        Group group = new Group();
        mockMvc.perform(post("/groups")
                        .flashAttr("group", group))
                .andExpect(model().attribute("group", group))
                .andExpect(view().name("groups/new"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/groups_and_students_filler.sql"})
    void show_ShouldContainModelAndReturnView() throws Exception {
        Group group = new Group();
        group.setId(1);
        group.setName("AB-12");
        mockMvc.perform(get("/groups/1"))
                .andExpect(model().attribute("group", group))
                .andExpect(view().name("groups/show"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/groups_and_students_filler.sql"})
    void index_ShouldContainModelsAndReturnView() throws Exception {
        mockMvc.perform(get("/groups"))
                .andExpect(view().name("groups/main"))
                .andExpect(model().attributeExists("pageNumbers"))
                .andExpect(model().size(2));
    }

    @Test
    @Sql(scripts = {"classpath:sql/groups_and_students_filler.sql"})
    void edit_ShouldContainModelsAndReturnView() throws Exception {
        Group group = new Group();
        group.setId(1);
        group.setName("AB-12");
        mockMvc.perform(get("/groups/1/edit")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(model().attributeExists("group"))
                .andExpect(view().name("groups/edit"));
    }

    @Test
    void edit_ShouldRestrictAccessIfUserHasNoRights() throws Exception {
        mockMvc.perform(get("/groups/1/edit")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(view().name("security/accessDenied"));
    }

    @Test
    void update_ShouldReturnView() throws Exception {
        Group group = new Group();
        group.setId(1);
        group.setName("abc");
        mockMvc.perform(MockMvcRequestBuilders.patch("/groups/1")
                        .flashAttr("group", group))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/groups"));
    }

    @Test
    void update_ShouldReturnView_WhenBindingResultHasErrors() throws Exception {
        Group group = new Group();
        mockMvc.perform(MockMvcRequestBuilders.patch("/groups/1")
                        .flashAttr("group", group))
                .andExpect(view().name("groups/edit"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/groups_and_students_filler.sql"})
    void delete_ShouldReturnView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/groups/1")
                        .sessionAttr("role", UserRole.ADMIN))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/groups"));
    }

    @Test
    void delete_ShouldRestrictAccess_IfUserHasNoRights() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/groups/1")
                        .sessionAttr("role", UserRole.STUDENT))
                .andExpect(view().name("security/accessDenied"));
    }
}