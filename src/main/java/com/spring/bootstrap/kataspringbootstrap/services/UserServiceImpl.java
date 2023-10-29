package com.spring.bootstrap.kataspringbootstrap.services;

import com.spring.bootstrap.kataspringbootstrap.model.User;
import com.spring.bootstrap.kataspringbootstrap.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(new User());
    }

    @Override
    @Transactional
    public boolean addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            System.out.println("Current user is present");
            return false;
        }

        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public void updateUser(int id, User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean deleteUserById(int id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (!userRepository.findByUsername(username).isPresent()) {
            throw  new UsernameNotFoundException(String.format("User with username (%s) not found", username));
        }
        else {
            return userRepository.findByUsername(username).get();
        }
    }
}