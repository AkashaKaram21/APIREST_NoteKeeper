package com.ra12.projecte1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ra12.projecte1.service.UserService;

import com.ra12.projecte1.dto.UserRequestDTO;
import com.ra12.projecte1.dto.UserResponseDTO;
import com.ra12.projecte1.model.User;

@RestController // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/image")
    public ResponseEntity<String> setUserImage(@RequestBody UserRequestDTO userRequest, @RequestBody MultipartFile imageFile) throws Exception {
        return userService.setUserImage(userRequest, imageFile);
    }

    @PostMapping("/image")
    public ResponseEntity<String> updateUser(@RequestBody long userId, User user) throws Exception {
        return userService.setUserImage(userRequest, imageFile);
    }
    
}
