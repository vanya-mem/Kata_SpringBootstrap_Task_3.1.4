package com.spring.bootstrap.kataspringbootstrap.controllers;

import com.spring.bootstrap.kataspringbootstrap.model.User;
import com.spring.bootstrap.kataspringbootstrap.repositories.RoleRepository;
import com.spring.bootstrap.kataspringbootstrap.services.UserService;
import com.spring.bootstrap.kataspringbootstrap.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Security;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository, UserValidator userValidator) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userValidator = userValidator;
    }

    @GetMapping()
    public String usersList(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) auth.getPrincipal();
        model.addAttribute("admin", admin);
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "users/index";
    }

    @GetMapping("/{id}")
    public String show(@RequestParam(value = "id", required = false) int id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "users/show";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "users/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleRepository.findAll());
            return "users/new";
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam(value = "id", required = false) int id) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleRepository.findAll());
        return "users/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @RequestParam(value = "id", required = false) int id,
                         BindingResult bindingResult, Model model) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleRepository.findAll());
            return "users/edit";
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete{id}")
    public String delete(@RequestParam(value = "id", required = false) int id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}

