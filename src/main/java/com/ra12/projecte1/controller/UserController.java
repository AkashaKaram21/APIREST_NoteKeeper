package com.ra12.projecte1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ra12.projecte1.service.UserService;
import com.ra12.projecte1.dto.UserRequestDTO;
import com.ra12.projecte1.dto.UserResponseDTO;
import com.ra12.projecte1.logging.UserLogging;
import com.ra12.projecte1.model.User;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserLogging userLogging;

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

    // Carrega massiva per csv
    @PostMapping("/upload-csv")
    public ResponseEntity<Map<String, Object>> uploadCSV(@RequestParam("file") MultipartFile file) {
        userLogging.logInfo("upload csv");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Guardar el archivo temporalmente
            File tempFile = File.createTempFile("users", ".csv");
            file.transferTo(tempFile);
            
            // Cargar datos desde el CSV
            int count = userService.loadFromCSV(tempFile.getAbsolutePath());
            
            // Eliminar archivo temporal
            tempFile.delete();
            
            response.put("message", "CSV cargado exitosamente");
            response.put("usersLoaded", count);
            userLogging.logInfo("CSV cargado:" + count);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            userLogging.logError("Error al procesar CSV: {}", e);
            response.put("error", "Error al procesar el archivo CSV");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Creacio d'usuari
    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        userLogging.logInfo("Endpoint POST /api/users llamado");
        
        try {
            User user = new User(userRequestDTO.getName(), userRequestDTO.getEmail(), userRequestDTO.getPassword());
            userService.createUser(user);
            
            UserResponseDTO response = new UserResponseDTO(user);
            userLogging.logInfo("Usuario creado: " + response);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            userLogging.logError("Error de validación: ", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            userLogging.logError("Error al crear usuario: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtenim tots els usuaris
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        userLogging.logInfo("Endpoint GET /api/users llamado");
        
        try {
            List<User> users = userService.getAllUsers();
            
            List<UserResponseDTO> response = users.stream()
                    .map(user -> new UserResponseDTO(user))
                    .collect(Collectors.toList());
            
            userLogging.logInfo("Retornando " + response.size() + " usuarios");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            userLogging.logError("Error al obtener usuarios: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtenim usuari a traves de la seva userId
    @GetMapping("/users/{userId}")
    public ResponseEntity<String> getUserByEmail(@PathVariable long userId) {
        return userService.getUser(userId);
    }
}
