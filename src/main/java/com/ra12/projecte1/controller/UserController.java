package com.ra12.projecte1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ra12.projecte1.dto.UserRequestDTO;
import com.ra12.projecte1.dto.UserResponseDTO;
import com.ra12.projecte1.model.User;
import com.ra12.projecte1.service.UserService;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/upload-csv")
    public ResponseEntity<Map<String, Object>> uploadCSV(@RequestParam("file") MultipartFile file) {
        logger.info("upload csv");
        
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
            logger.info("CSV cargado:", count);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            logger.error("Error al procesar CSV: {}", e.getMessage());
            response.put("error", "Error al procesar el archivo CSV");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        logger.info("Endpoint POST /api/users llamado");
        
        try {
            User user = new User(userRequestDTO.getEmail(), userRequestDTO.getPassword());
            User createdUser = userService.createUser(user);
            
            UserResponseDTO response = new UserResponseDTO(createdUser.getEmail());
            logger.info("Usuario creado: {}", createdUser.getEmail());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            logger.error("Error de validación: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error al crear usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        logger.info("Endpoint GET /api/users llamado");
        
        try {
            List<User> users = userService.getAllUsers();
            
            List<UserResponseDTO> response = users.stream()
                    .map(user -> new UserResponseDTO(user.getEmail()))
                    .collect(Collectors.toList());
            
            logger.info("Retornando {} usuarios", response.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error al obtener usuarios: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        logger.info("email", email);
        
        try {
            Optional<User> user = userService.getUserByEmail(email);
            
            if (user.isPresent()) {
                UserResponseDTO response = new UserResponseDTO(user.get().getEmail());
                logger.info("Usuario encontrado: ", email);
                return ResponseEntity.ok(response);
            } else {
                logger.warn("Usuario no encontrado: ", email);
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            logger.error("Error al buscar usuario: ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}