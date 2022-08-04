package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.*;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.models.User;
import ua.com.foxminded.university.models.enums.UserRole;
import ua.com.foxminded.university.service.UserService;
import ua.com.foxminded.university.service.impl.EncryptionService;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginControllerTest {

    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @InjectMocks
    private LoginController loginController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    void login_ShouldReturnView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.view().name("security/login"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
    }

    @Test
    void approve_ShouldReturnMainPageIfUserExists() throws Exception {
        User user = new User(1, "test", "1", UserRole.ADMIN, 1);
        MockedStatic<EncryptionService> utilities = mockStatic(EncryptionService.class);
        utilities.when(() -> EncryptionService.match(anyString(), anyString()))
                .thenReturn(true);
        when(userService.checkIfExists(user.getLogin())).thenReturn(true);
        when(userService.findByLogin(user.getLogin())).thenReturn(user);
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .flashAttr("user", user)
                        .session(mockHttpSession))
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @Test
    void approve_ShouldReturnFailedLoginViewIfUserIsNotExists() throws Exception {
        User user = new User(1, "test", "1", UserRole.ADMIN, 1);
        when(userService.checkIfExists(user.getLogin())).thenReturn(false);
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .flashAttr("user", user)
                        .session(mockHttpSession))
                .andExpect(MockMvcResultMatchers.view().name("security/failedLogin"));
    }

}