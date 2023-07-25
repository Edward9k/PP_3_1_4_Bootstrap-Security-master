package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/user")
    public String getPersonalPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/admin")
    public String getAdminPage(@AuthenticationPrincipal User user, Model model) {
        List<User> list = userService.getAllUsers();
        model.addAttribute("listOfUsers", list);
        model.addAttribute("admin", user);
        model.addAttribute("newUser", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin";
    }

    @PostMapping("/createUser")
    public String createUserInDb(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return "redirect:/admin/admin";
    }

    @PatchMapping("/admin/{id}")
    public String applyUserUpdate(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin/admin";
    }

    @DeleteMapping("/admin/{id}/delete")
    public String deleteUserById(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return "redirect:/admin/admin";
    }
}
