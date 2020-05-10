package web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public ModelAndView allUsers(ModelMap model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/users");
        return modelAndView;
    }
    @GetMapping(value = "/admin/users")
    public ModelAndView getUsers() {
        List<User> users = userService.listAllUsers();
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Role> allRoles = userService.rolesList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");
        modelAndView.addObject("users", users);
        modelAndView.addObject("userauth", userAuth);
        modelAndView.addObject("allRoles", allRoles);
        return modelAndView;
    }

    @GetMapping(value = {"/admin/add"})
    public String showAddUser(Model model) {
        String[] allRoles = {"admin", "user", "anonym"};
        List<Role> roles = userService.rolesList();
        User user = new User();
        model.addAttribute("add", true);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("checkedRoles", allRoles);
        model.addAttribute("add", true);

        return "edit";    }

    @PostMapping(value = "/admin/add")
    public ModelAndView addUser(@ModelAttribute("user") User user, @RequestParam("checkedRoles") String[] checkedRoles) {
        ModelAndView modelAndView = new ModelAndView();
        try {

            User userDS = new User();
            userDS = userService.getUserByName(user.getUsername());
            if (userDS != null) {
                return modelAndView;
            }
            modelAndView.setViewName("redirect:/admin");
            List<Role> roles = new ArrayList<>();
            for (String role : checkedRoles) {
                roles.add(userService.getRoleByName(role));
            }
            user.setRoles(roles);
            userService.add(user);
            return modelAndView;
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            modelAndView.addObject("errorMessage", errorMessage);
            //model.addAttribute("contact", contact);
            modelAndView.addObject("add", true);
            return modelAndView;
        }
    }

    @GetMapping(value = {"/admin/edit/{userId}"})
    public ModelAndView editUser(@PathVariable long userId) {
        User user = userService.getUserById(userId);
        List<Role> roles = userService.rolesList();
        String[] allRoles = {"admin", "user", "anonym"};

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit");
        modelAndView.addObject("roles", roles);
        modelAndView.addObject("user", user);
        modelAndView.addObject("checkedRoles", allRoles);
        modelAndView.addObject("add", false);
        return modelAndView;
    }

    @PostMapping(value = {"/admin/edit/{userId}"})
    public ModelAndView updateUser(@PathVariable long userId,
                             @ModelAttribute("user") User user,
                             @RequestParam(name="checkedRoles") String[] checkedRoles) {
        user.setId(userId);
        List<Role> listRoles = userService.rolesList();
        ModelAndView modelAndView = new ModelAndView();
        //modelAndView.setViewName("redirect:/admin");
        List<Role> roles = new ArrayList<>();
        for(String role : checkedRoles){
            roles.add(userService.getRoleByName(role));
        }
       user.setRoles(roles);
        if (checkedRoles.length < 1) {
            modelAndView.setViewName("edit");
            modelAndView.addObject("listRoles", listRoles);
            modelAndView.addObject("errorMessage","Choose the role or roles");
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        if (user.getPassword().equals("") || user.getUsername().equals("")) {
            modelAndView.setViewName("edit");
            modelAndView.addObject("listRoles", listRoles);
            modelAndView.addObject("errorMessage", "Some field is empty");
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        userService.update(user);
        modelAndView.setViewName("redirect:/admin");
        return modelAndView;
    }


    @GetMapping(value = {"/admin/delete/{userId}"})
    public ModelAndView showDeleteContactById(
            Model model, @PathVariable long userId) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getUserById(userId);
        userService.delete(user);
        modelAndView.setViewName("redirect:/admin");
        return modelAndView;
    }


    @GetMapping("/user")
    public ModelAndView home(Authentication auth, ModelAndView mav) {
        User user = userService.getUserByName(auth.getName());
        mav.addObject("user", user);
        Role roleAdmin = new Role();
        roleAdmin = userService.getRoleByName("admin");
        mav.addObject("roleAdmin", roleAdmin);
        mav.setViewName("user");
        return mav;
    }
}


