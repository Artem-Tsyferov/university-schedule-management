package ua.com.foxminded.university.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.foxminded.university.models.enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class RoleCheckerUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleCheckerUtils.class);

    private RoleCheckerUtils() {
    }

    public static String checkAccess(HttpServletRequest request, UserRole role) {
        HttpSession session = request.getSession();
        LOGGER.debug("Checking access right");
        if (!session.getAttribute("role").equals(role)) {
            LOGGER.debug("Access denied");
            return "security/accessDenied";
        }
        LOGGER.debug("Access granted");
        return null;
    }

    public static String checkAccessForTwoRoles(HttpServletRequest request, UserRole role1, UserRole role2) {
        HttpSession session = request.getSession();
        LOGGER.debug("Checking access right");
        if (!session.getAttribute("role").equals(role1) && !session.getAttribute("role").equals(role2)) {
            LOGGER.debug("Access denied");
            return "security/accessDenied";
        }
        LOGGER.debug("Access granted");
        return null;
    }
}
