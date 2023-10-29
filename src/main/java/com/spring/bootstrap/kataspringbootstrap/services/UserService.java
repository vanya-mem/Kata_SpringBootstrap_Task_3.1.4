package com.spring.bootstrap.kataspringbootstrap.services;

import com.spring.bootstrap.kataspringbootstrap.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    User getUserById(int id);

    boolean addUser(User user);

    void updateUser(int id, User user);

    boolean deleteUserById(int id);

}
