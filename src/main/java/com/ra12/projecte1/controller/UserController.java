package com.ra12.projecte1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    // Endpoint de debug perque no tinc el codi d'afegir / obtenir usuaris
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Endpoint per pujar l'imatge d'un usuari a través del seu DTO de request (email i password)
    @PostMapping("/users/image")
    public ResponseEntity<String> setUserImage(@RequestParam String email, @RequestParam String password, @RequestParam MultipartFile imageFile) throws Exception {
        UserRequestDTO userRequest = new UserRequestDTO(email, password);

        return userService.setUserImage(userRequest, imageFile);
    }

    // Endpoint per actualitzar un usuari a través de la seva userId
    // Es pasa un user amb atributs canviats i s'apliquen es canvis si el valor no es null i si son diferents
    @PutMapping("/users/{userId}/update")
    public ResponseEntity<String> updateUser(@RequestBody User user, @PathVariable long userId) throws Exception {
        return userService.updateUser(userId, user);
    }

    // Endpoint per borrar un usuari a través de la seva userId
    @DeleteMapping("/users/{userId}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable long userId) throws Exception {
        return userService.deleteUser(userId);
    }

    // Endpoint per borrar tots els usuaris
    @DeleteMapping("/users/delete")
    public ResponseEntity<String> deleteAllUsers() throws Exception {
        return userService.deleteAllUsers();
    }
}
