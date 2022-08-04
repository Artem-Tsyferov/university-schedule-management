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
import ua.com.foxminded.university.models.Group;
import ua.com.foxminded.university.models.enums.UserRole;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.utils.validator.GroupValidator;
import ua.com.foxminded.university.utils.RoleCheckerUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);
    private final GroupService groupService;
    private final GroupValidator groupValidator;

    @Autowired
    public GroupController(GroupService groupService, GroupValidator groupValidator) {
        this.groupService = groupService;
        this.groupValidator = groupValidator;
    }

    @GetMapping("/new")
    public String newGroup(Model model,
                           HttpServletRequest request) {
        String restriction = RoleCheckerUtils.checkAccess(request, UserRole.ADMIN);
        if (restriction != null) {
            return restriction;
        }
        model.addAttribute("group", new Group());
        return "groups/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("group") @Valid Group group,
                         BindingResult bindingResult) {
        groupValidator.validate(group, bindingResult);
        if (bindingResult.hasErrors()) {
            return "groups/new";
        }
        LOGGER.debug("Saving {}", group);
        groupService.save(group);
        return "redirect:/groups";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        LOGGER.debug("Finding Group with id = {}", id);
        model.addAttribute("group", groupService.findById(id));
        return "groups/show";
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Group> groupPage = groupService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("groupPage", groupPage);
        int totalPages = groupPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "groups/main";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,
                       @PathVariable("id") int id,
                       HttpServletRequest request) {
        String restriction = RoleCheckerUtils.checkAccess(request, UserRole.ADMIN);
        if (restriction != null) {
            return restriction;
        }
        model.addAttribute("group", groupService.findById(id));
        return "groups/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("group") @Valid Group group,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "groups/edit";
        }
        LOGGER.debug("Updating {}", group);
        groupService.update(group);
        return "redirect:/groups";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id,
                         HttpServletRequest request) {
        String restriction = RoleCheckerUtils.checkAccess(request, UserRole.ADMIN);
        if (restriction != null) {
            return restriction;
        }
        LOGGER.debug("Deleting Group with id = {}", id);
        groupService.deleteById(id);
        return "redirect:/groups";
    }
}
