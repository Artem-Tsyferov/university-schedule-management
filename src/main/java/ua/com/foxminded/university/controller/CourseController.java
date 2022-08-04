package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.models.Course;
import ua.com.foxminded.university.models.enums.UserRole;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.utils.validator.CourseValidator;
import ua.com.foxminded.university.utils.RoleCheckerUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequestMapping("/courses")
public class CourseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;
    private final CourseValidator courseValidator;

    @Autowired
    public CourseController(CourseService courseService, CourseValidator courseValidator) {
        this.courseService = courseService;
        this.courseValidator = courseValidator;
    }

    @GetMapping("/new")
    public String newCourse(Model model,
                            HttpServletRequest request) {
        String restriction = RoleCheckerUtils.checkAccess(request, UserRole.ADMIN);
        if (restriction != null) {
            return restriction;
        }
        model.addAttribute("course", new Course());
        return "courses/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("course") @Valid Course course,
                         BindingResult bindingResult) {
        courseValidator.validate(course, bindingResult);
        if (bindingResult.hasErrors()) {
            return "courses/new";
        }
        LOGGER.debug("Saving {}", course);
        courseService.save(course);
        return "redirect:/courses";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,
                       Model model) {
        LOGGER.debug("Finding Course with id = {}", id);
        model.addAttribute("course", courseService.findById(id));
        return "courses/show";
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Course> coursePage = courseService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("coursePage", coursePage);
        int totalPages = coursePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "courses/main.html";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,
                       @PathVariable("id") int id,
                       HttpServletRequest request) {
        String restriction = RoleCheckerUtils.checkAccess(request, UserRole.ADMIN);
        if (restriction != null) {
            return restriction;
        }
        model.addAttribute("course", courseService.findById(id));
        return "courses/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("course") @Valid Course course,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "courses/edit";
        }
        LOGGER.debug("Updating {}", course);
        courseService.update(course);
        return "redirect:/courses";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id,
                         HttpServletRequest request) {
        String restriction = RoleCheckerUtils.checkAccess(request, UserRole.ADMIN);
        if (restriction != null) {
            return restriction;
        }
        LOGGER.debug("Deleting Course with id = {}", id);
        courseService.deleteById(id);
        return "redirect:/courses";
    }
}
