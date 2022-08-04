package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.models.User;
import ua.com.foxminded.university.service.UserService;
import ua.com.foxminded.university.service.impl.EncryptionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/login")
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String login(Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        model.addAttribute("user", new User());
        return "security/login";
    }

    @PostMapping()
    public String approve(@ModelAttribute("user") User user,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        LOGGER.debug("Attempting to authenticate");
        String username = user.getLogin();
        String password = user.getPassword();
        if (userService.checkIfExists(username) &&
                EncryptionService.match(password, userService.findByLogin(username).getPassword())) {
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("role", userService.findByLogin(username).getRole());
            newSession.setMaxInactiveInterval(5 * 60);

            Cookie message = new Cookie("message", "welcome");
            response.addCookie(message);
            LOGGER.debug("Authentication success");
            return "index";
        } else {
            LOGGER.info("Authentication failed");
            return "security/failedLogin";
        }
    }
}
