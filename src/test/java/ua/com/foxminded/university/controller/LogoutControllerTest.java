package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LogoutControllerTest {

    private MockMvc mockMvc;
    @InjectMocks
    private LogoutController logoutController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(logoutController).build();
    }

    @Test
    void logout_ShouldInvalidateSessionAndReturnView() throws Exception {
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockMvc.perform(MockMvcRequestBuilders.get("/logout")
                .session(mockHttpSession))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login"));
    }
}