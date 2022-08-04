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
import ua.com.foxminded.university.models.Student;
import ua.com.foxminded.university.models.enums.UserRole;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;
import ua.com.foxminded.university.utils.RoleCheckerUtils;
import ua.com.foxminded.university.utils.validator.StudentValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;
    private final GroupService groupService;
    private final StudentValidator studentValidator;

    @Autowired
    public StudentController(StudentService studentService, GroupService groupService, StudentValidator studentValidator) {
        this.studentService = studentService;
        this.groupService = groupService;
        this.studentValidator = studentValidator;
    }

    @GetMapping("/new")
    public String newStudent(Model model,
                             HttpServletRequest request) {
        String restriction = RoleCheckerUtils.checkAccess(request, UserRole.ADMIN);
        if (restriction != null) {
            return restriction;
        }
        model.addAttribute("student", new Student());
        model.addAttribute("groups", groupService.findAllGroups());
        return "students/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("student") @Valid Student student,
                         BindingResult bindingResult,
                         Model model) {
        studentValidator.validate(student, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("groups", groupService.findAllGroups());
            return "students/new";
        }
        LOGGER.debug("Saving {}", student);
        studentService.save(student);
        return "redirect:/students";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        LOGGER.debug("Finding Student with id = {}", id);
        model.addAttribute("student", studentService.findById(id));
        return "students/show";
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Student> studentPage = studentService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("studentPage", studentPage);
        int totalPages = studentPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "students/main";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,
                       @PathVariable("id") int id,
                       HttpServletRequest request) {
        String restriction = RoleCheckerUtils.checkAccess(request, UserRole.ADMIN);
        if (restriction != null) {
            return restriction;
        }
        model.addAttribute("student", studentService.findById(id));
        model.addAttribute("groups", groupService.findAllGroups());
        return "students/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("student") @Valid Student student,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("groups", groupService.findAllGroups());
            return "students/edit";
        }
        LOGGER.debug("Updating {}", student);
        studentService.update(student);
        return "redirect:/students";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id,
                         HttpServletRequest request) {
        String restriction = RoleCheckerUtils.checkAccess(request, UserRole.ADMIN);
        if (restriction != null) {
            return restriction;
        }
        LOGGER.debug("Deleting Student with id = {}", id);
        studentService.deleteById(id);
        return "redirect:/students";
    }
}
