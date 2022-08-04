package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.models.Course;
import ua.com.foxminded.university.models.Teacher;
import ua.com.foxminded.university.models.enums.UserRole;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.utils.RoleCheckerUtils;
import ua.com.foxminded.university.utils.validator.TeacherValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherController.class);
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final TeacherValidator teacherValidator;

    public TeacherController(TeacherService teacherService, CourseService courseService, TeacherValidator teacherValidator) {
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.teacherValidator = teacherValidator;
    }

    @GetMapping("/new")
    public String newTeacher(Model model,
                             HttpServletRequest request) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("courses", courseService.findAllCourses());
        String restriction = RoleCheckerUtils.checkAccess(request, UserRole.ADMIN);
        if (restriction != null) {
            return restriction;
        }
        return "teachers/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("teacher") @Valid Teacher teacher,
                         BindingResult bindingResult,
                         @RequestParam(value = "selectedCourses", required = false) int[] courses,
                         Model model) {
        teacherValidator.validate(teacher, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", courseService.findAllCourses());
            return "teachers/new";
        }
        setCoursesIfExists(teacher, courses);
        LOGGER.debug("Saving {}", teacher);
        teacherService.save(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        LOGGER.debug("Finding Teacher with id = {}", id);
        model.addAttribute("teacher", teacherService.findById(id));
        return "teachers/show";
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Teacher> teacherPage = teacherService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("teacherPage", teacherPage);
        int totalPages = teacherPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "teachers/main";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,
                       @PathVariable("id") int id,
                       HttpServletRequest request) {
        String restriction = RoleCheckerUtils.checkAccess(request, UserRole.ADMIN);
        if (restriction != null) {
            return restriction;
        }
        model.addAttribute("teacher", teacherService.findById(id));
        model.addAttribute("courses", courseService.findAllCourses());
        return "teachers/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("teacher") @Valid Teacher teacher,
                         BindingResult bindingResult,
                         @RequestParam(value = "selectedCourses", required = false) int[] courses,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", courseService.findAllCourses());
            return "teachers/edit";
        }
        setCoursesIfExists(teacher, courses);
        LOGGER.debug("Updating {}", teacher);
        teacherService.update(teacher);
        return "redirect:/teachers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id,
                         HttpServletRequest request) {
        String restriction = RoleCheckerUtils.checkAccess(request, UserRole.ADMIN);
        if (restriction != null) {
            return restriction;
        }
        LOGGER.debug("Deleting Teacher with id = {}", id);
        teacherService.deleteById(id);
        return "redirect:/teachers";
    }

    private void setCoursesIfExists(Teacher teacher, int[] courses) {
        if (courses != null) {
            List<Course> courseList = new ArrayList<>();
            for (int course : courses) {
                courseList.add(courseService.findById(course));
            }
            teacher.setCourses(courseList);
        }
    }
}
