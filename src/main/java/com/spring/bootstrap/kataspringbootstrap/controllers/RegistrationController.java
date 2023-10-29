package com.spring.bootstrap.kataspringbootstrap.controllers;

import com.spring.bootstrap.kataspringbootstrap.model.User;
import com.spring.bootstrap.kataspringbootstrap.repositories.RoleRepository;
import com.spring.bootstrap.kataspringbootstrap.services.UserService;
import com.spring.bootstrap.kataspringbootstrap.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final UserService userService;

    private final UserValidator userValidator;

    private final RoleRepository roleRepository;

    @Autowired
    public RegistrationController(UserService userService, UserValidator userValidator, RoleRepository roleRepository) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "/registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid User user, BindingResult bindingResult, Model model) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleRepository.findAll());
            return "/registration";
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.addUser(user);

        return "redirect:/user";
    }
}
