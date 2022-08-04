package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.models.*;
import ua.com.foxminded.university.models.enums.UserRole;
import ua.com.foxminded.university.dto.LectureDTO;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LectureService;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.utils.validator.LectureValidator;
import ua.com.foxminded.university.utils.RoleCheckerUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/lectures")
public class LectureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LectureController.class);
    private final LectureService lectureService;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final CourseService courseService;
    private final LectureValidator lectureValidator;

    @Autowired
    public LectureController(LectureService lectureService, TeacherService teacherService, GroupService groupService, CourseService courseService, LectureValidator lectureValidator) {
        this.lectureService = lectureService;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.courseService = courseService;
        this.lectureValidator = lectureValidator;
    }


    @GetMapping("/new")
    public String newLecture(Model model,
                             HttpServletRequest request,
                             @ModelAttribute("chosenCourse") Course course,
                             @ModelAttribute("chosenGroup") Group group,
                             @ModelAttribute("chosenTeacher") Teacher teacher) {
        String restriction = RoleCheckerUtils.checkAccessForTwoRoles(request, UserRole.ADMIN, UserRole.TEACHER);
        if (restriction != null) return restriction;
        model.addAttribute("lectureDTO", new LectureDTO());
        model.addAttribute("teachers", teacherService.findAllTeachers());
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("courses", courseService.findAllCourses());
        return "lectures/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("lectureDTO") @Valid LectureDTO lectureDTO,
                         BindingResult bindingResult,
                         Model model) {
        lectureValidator.validate(lectureDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("teachers", teacherService.findAllTeachers());
            model.addAttribute("groups", groupService.findAllGroups());
            model.addAttribute("courses", courseService.findAllCourses());
            return "lectures/new";
        }
        Lecture lecture = new Lecture();
        lecture.setCourse(courseService.findById(lectureDTO.getCourseId()));
        lecture.setGroup(groupService.findById(lectureDTO.getGroupId()));
        lecture.setTeacher(teacherService.findById(lectureDTO.getTeacherId()));
        lecture.setDate(lectureDTO.getDate());
        lecture.setRoomNumber(lectureDTO.getRoomNumber());
        lecture.setTimeOfStart(lectureDTO.getTimeOfStart());
        lecture.setTimeOfEnd(lectureDTO.getTimeOfEnd());
        LOGGER.debug("Saving {}", lecture);
        lectureService.save(lecture);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        LOGGER.debug("Finding Lecture with id = {}", id);
        model.addAttribute("lecture", lectureService.findById(id));
        return "lectures/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id,
                       HttpServletRequest request,
                       @ModelAttribute("chosenCourse") Course course,
                       @ModelAttribute("chosenGroup") Group group,
                       @ModelAttribute("chosenTeacher") Teacher teacher) {
        String restriction = RoleCheckerUtils.checkAccessForTwoRoles(request, UserRole.ADMIN, UserRole.TEACHER);
        if (restriction != null) {
            return restriction;
        }
        model.addAttribute("lecture", lectureService.findById(id));
        model.addAttribute("lectureDTO", new LectureDTO());
        model.addAttribute("teachers", teacherService.findAllTeachers());
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("courses", courseService.findAllCourses());
        return "lectures/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("lectureDTO") @Valid LectureDTO lectureDTO,
                         BindingResult bindingResult,
                         @ModelAttribute("lecture") Lecture lecture,
                         Model model) {
        lectureValidator.validate(lectureDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("teachers", teacherService.findAllTeachers());
            model.addAttribute("groups", groupService.findAllGroups());
            model.addAttribute("courses", courseService.findAllCourses());
            return "lectures/edit";
        }
        lecture.setId(lectureDTO.getId());
        lecture.setCourse(courseService.findById(lectureDTO.getCourseId()));
        lecture.setGroup(groupService.findById(lectureDTO.getGroupId()));
        lecture.setTeacher(teacherService.findById(lectureDTO.getTeacherId()));
        LOGGER.debug("Updating {}", lecture);
        lectureService.update(lecture);
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id,
                         HttpServletRequest request) {
        String restriction = RoleCheckerUtils.checkAccessForTwoRoles(request, UserRole.ADMIN, UserRole.TEACHER);
        if (restriction != null) {
            return restriction;
        }
        LOGGER.debug("Deleting Lecture with id = {}", id);
        lectureService.deleteById(id);
        return "redirect:/";
    }
}
