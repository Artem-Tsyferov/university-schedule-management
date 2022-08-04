package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import ua.com.foxminded.university.models.User;
import ua.com.foxminded.university.service.UserService;
import ua.com.foxminded.university.utils.validator.UserValidator;

import static org.mockito.Mockito.doNothing;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrationControllerTest {

    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Mock
    private UserValidator userValidator;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private RegistrationController registrationController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
    }

    @Test
    void reg_ShouldContainModelAndReturnView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/registration"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.view().name("security/registration"));
    }

    @Test
    void createUser_ShouldReturnView() throws Exception {
        User user = new User();
        user.setLogin("a");
        user.setPassword("a");
        user.setPersonId(1);
        doNothing().when(userValidator).validate(user, bindingResult);
        doNothing().when(userService).save(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                        .flashAttr("user", user))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login"));
    }

    @Test
    void createUser_ShouldReturnView_WhenBindingResultHasErrors() throws Exception {
        User user = new User();
        doNothing().when(userService).save(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                        .flashAttr("user", user))
                .andExpect(MockMvcResultMatchers.view().name("security/registration"));
    }
}