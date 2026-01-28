package com.ra12.projecte1.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ra12.projecte1.model.User;
import com.ra12.projecte1.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;


    //Crear usuario
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            if (user.getName() == null || user.getName().isEmpty() ||
                user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Todos los campos obligatorios deben ser completados: name, email, password");
            }

            Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
            user.setDataCreated(currentTime);
            user.setDataUpdated(currentTime);

            int result = userService.createUser(user);

            if (result > 0) {
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Usuario creado exitosamente con ID: " + user.getId());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el usuario");
            }

        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("email")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El email '" + user.getEmail() + "' ya está registrado");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al crear usuario: " + e.getMessage());
        }
    }

     // Obtener todos los usuarios
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            if (users == null || users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener usuario por ID
    @GetMapping("/users/{user_id}")
    public ResponseEntity<User> getUserById(@PathVariable("user_id") long user_id) {
        try {
            User user = userService.getUserById(user_id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}