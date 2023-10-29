package com.spring.bootstrap.kataspringbootstrap.validators;

import com.spring.bootstrap.kataspringbootstrap.model.User;
import com.spring.bootstrap.kataspringbootstrap.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (user.getRoles() != null) {
            if (user.getRoles().size() == 0) {
                errors.rejectValue("roles", "", "None roles selected");
            }
        }
        if (user.getPassword().length() < 4) {
            errors.rejectValue("password", "", "Password must be longer or equal to 4 chars");
        }
        if (user.getUsername().equals("")) {
            errors.rejectValue("username", "", "Username can't be empty");
        }
        else {
            try {
                userService.loadUserByUsername(user.getUsername());
            } catch (UsernameNotFoundException e) {
                System.out.println("uzer ne naiden no tu dolbaeb pochemy to");
                return;
            }
        }
    }
}
