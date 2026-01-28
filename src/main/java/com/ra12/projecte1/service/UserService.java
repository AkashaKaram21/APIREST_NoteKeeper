package com.ra12.projecte1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ra12.projecte1.model.User;
import com.ra12.projecte1.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public int loadFromCSV(String filePath) {
        logger.info("Iniciando carga de usuarios desde CSV: {}", filePath);
        try {
            int count = userRepository.loadFromCSV(filePath);
            logger.info("Carga completada exitosamente. Total de usuarios cargados: {}", count);
            return count;
        } catch (IOException e) {
            logger.error("Error al cargar CSV:", e.getMessage());
            throw new RuntimeException("Error al cargar el archivo CSV", e);
        }
    }

    public User createUser(User user) {
        logger.info("Creando nuevo usuario con email: ", user.getEmail());

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            logger.error("Error: El email no puede estar vacío");
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            logger.error("Error: El password no puede estar vacío");
            throw new IllegalArgumentException("El password no puede estar vacío");
        }

        try {
            userRepository.save(user);
            logger.info("Usuario creado exitosamente: {}", user.getEmail());
            return user;
        } catch (Exception e) {
            logger.error("Error al crear usuario: {}", e.getMessage());
            throw new RuntimeException("Error al crear el usuario", e);
        }
    }

    public List<User> getAllUsers() {
        logger.info("Obteniendo todos los usuarios");
        try {
            List<User> users = userRepository.findAll();
            logger.info("Se encontraron {} usuarios", users.size());
            return users;
        } catch (Exception e) {
            logger.error("[cError al obtener usuarios: {}", e.getMessage());
            throw new RuntimeException("Error al obtener los usuarios", e);
        }

    public Optional<User> getUserByEmail(String email) {
        logger.info("Buscando usuario con email: {}", email);
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                logger.info("Usuario encontrado: {}", email);
            } else {
                logger.warn("Usuario no encontrado: {}", email);
            }
            return user;
        } catch (Exception e) {
            logger.error("Error al buscar usuario: {}", e.getMessage());
            throw new RuntimeException("Error al buscar el usuario", e);
        }
    }
 }
}
