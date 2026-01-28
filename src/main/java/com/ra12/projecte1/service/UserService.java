package com.ra12.projecte1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra12.projecte1.model.User;
import com.ra12.projecte1.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

     private final ObjectMapper mapper = new ObjectMapper();

    // Métodos CRUD básicos
    public int createUser(User user) {
        return userRepository.insertUser(user);
    }

    public java.util.List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User getUserById(long userId) {
        return userRepository.getUserById(userId);
    }

}
