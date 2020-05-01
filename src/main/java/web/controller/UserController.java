package web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
//    @GetMapping(value = {"/"})
//    public String index(Model model) {
//
//        return "login";
//    }
    @GetMapping("/admin")
    public ModelAndView allUsers(ModelMap model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/users");
        return modelAndView;
    }
    @GetMapping(value = "/users")
    public ModelAndView getUsers() {
        List<User> users = userService.listAllUsers();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");
        modelAndView.addObject("users", users);
        return modelAndView;
//        model.addAttribute("hasPrev", hasPrev);
//        model.addAttribute("prev", pageNumber - 1);
//        model.addAttribute("hasNext", hasNext);
//        model.addAttribute("next", pageNumber + 1);

    }

    @GetMapping(value = "/user/{userId}")
    public String getContactById(Model model, @PathVariable long userId) {
        return null;
    }

    @GetMapping(value = {"/admin/add"})
    public String showAddContact(Model model) {
        User user = new User();
        model.addAttribute("add", true);
        model.addAttribute("user", user);
        return "edit";    }

    @PostMapping(value = "/admin/add")
    public ModelAndView addUser(@ModelAttribute("user") User user, @RequestParam("checkedRoles") String[] checkedRoles) {
        ModelAndView modelAndView = new ModelAndView();
        User userDS = new User();
        userDS = userService.getUserByName(user.getUsername());
        if (userDS != null) {
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/admin");
        List<Role> roles = new ArrayList<>();
        for(String role : checkedRoles){
            roles.add(userService.getRoleByName(role));
        }
        user.setRoles(roles);
        userService.add(user);
        return modelAndView;
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
        List<Role> listRoles = userService.rolesList();
        ModelAndView modelAndView = new ModelAndView();
        //modelAndView.setViewName("redirect:/admin");
        List<Role> roles = new ArrayList<>();
        for (int i = 0; i < checkedRoles.length; i++) {
            roles.add(new Role(checkedRoles[i]));
        }
        user.setRoles(roles);
        if (checkedRoles.length < 2) {
//            modelAndView.setViewName("edit");
//            modelAndView.addObject("listRoles", listRoles);
//            modelAndView.addObject("error","Choose the role or roles");
//            modelAndView.addObject("user", user);
            return modelAndView;
        }
        if (user.getPassword().equals("") || user.getUsername().equals("")) {
//            modelAndView.setViewName("edit");
//            modelAndView.addObject("listRoles", listRoles);
//            modelAndView.addObject("error", "Some field is empty");
//            modelAndView.addObject("user", user);
            return modelAndView;
        }
        userService.update(user);
        modelAndView.setViewName("redirect:/admin" + String.valueOf(user.getId()));
        return modelAndView;
    }


    @GetMapping(value = {"/admin/delete/{userId}"})
    public String showDeleteContactById(
            Model model, @PathVariable long userId) {
        User user = userService.getUserById(userId);
        userService.delete(user);
        //model.addAttribute("user", user);
        return "redirect:/admin";
    }

//    @PostMapping(value = {"/admin/delete/{userId}"})
//    public String deleteContactById(
//            Model model, @PathVariable long userId) {
//        User user = userService.getUserById(userId);
//        userService.delete(user);
//        return "redirect:/admin";
//    }

    @GetMapping("/user")
    public ModelAndView home(Authentication auth, ModelAndView mav) {
        mav.addObject("user", userService.getUserByName(auth.getName()));
        mav.setViewName("user");
        return mav;
    }
}


