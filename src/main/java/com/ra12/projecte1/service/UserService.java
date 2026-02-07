package com.ra12.projecte1.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ra12.projecte1.dto.UserRequestDTO;
import com.ra12.projecte1.dto.UserResponseDTO;
import com.ra12.projecte1.model.User;
import com.ra12.projecte1.logging.UserLogging;
import com.ra12.projecte1.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserLogging userLogging;

    // A través del userRequest (email i contrasenya) asignem una imatge i la
    // guardem
    public ResponseEntity<String> setUserImage(UserRequestDTO userRequest, MultipartFile imageFile) throws Exception {
        User user = userRepository.getUserByUserRequestDTO(userRequest);

        if (user == null) {
            userLogging.logError("L'usuari amb el email: " + userRequest.getEmail() + " no existeix", null);

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No s'ha pogut trobar l'usuari.");
        }

        userLogging.logInfo("Afegint la imatge " + (imageFile != null ? imageFile.getName() : "null")
                + " per l'usuari amb id: " + user.getId());

        String imagePath = null;

        if (imageFile != null) {
            File imageFolder = new File("src/main/resources/private/images");

            if (!imageFolder.exists()) {
                imageFolder.mkdirs();
            }

            imagePath = "src/main/resources/private/images/" + user.getId() + ".png";

            File image = new File("src/main/resources/private/images/" + user.getId() + ".png");

            try (OutputStream outputStream = new FileOutputStream(image)) {
                outputStream.write(imageFile.getBytes());
            }
        }

        user.setImagePath(imagePath);

        userRepository.updateUser(user);

        userLogging.logInfo("La imatge s'ha guardat correctament. El path és: " +
                imagePath);

        return ResponseEntity.status(HttpStatus.OK)
                .body("S'ha pujat la foto de l'usuari: " + new UserResponseDTO(user) + " al path: " + imagePath);
    }

    // Funcio per actualitzar un usuari per la seva id i un "usuari actualitzat"
    // Si l'usuari existeix i els canvis no son nulls i són diferents als valors
    // actuals, es cambiaran
    public ResponseEntity<String> updateUser(long userId, User updatedUser) throws Exception {
        userLogging.logInfo("Actualitzant usuari amb la id: " + userId);

        User user = userRepository.getUserById(userId);

        if (user == null) {
            userLogging.logError("L'usuari amb id: " + userId + " no existeix", null);

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No s'ha pogut trobar l'usuari.");
        }

        if (updatedUser.getName() != null && !updatedUser.getName().equals(user.getName())) {
            user.setName(updatedUser.getName());
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(user.getEmail())) {
            user.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().equals(user.getPassword())) {
            user.setPassword(updatedUser.getPassword());
        }

        userRepository.updateUser(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Usuari actualitzat!");
    }

    // Funcio per borrar un usuari per la seva id
    public ResponseEntity<String> deleteUser(long userId) {
        userLogging.logInfo("Borrant al usuari amb id: " + userId);

        try {
            userRepository.deleteUser(userId);
        } catch (Exception exception) {
            userLogging.logError("L'usuari amb id: " + userId + " no existeix",
                    exception);

            return ResponseEntity.status(HttpStatus.CONFLICT).body("No s'ha pogut esborrar l'Usuari amb id: \""
                    + userId + "\", error: " + exception.getLocalizedMessage());
        }

        userLogging.logInfo("L'usuari amb id: " + userId + " s'ha borrat correctament");

        return ResponseEntity.status(HttpStatus.OK).body("S'ha esborrat el nom de l'usuari amb id: " + userId);
    }

    // Funcio per borrar tots els usuaris
    public ResponseEntity<String> deleteAllUsers() {
        userLogging.logInfo("Borrant tots els usuaris");
        userRepository.deleteAllUsers();

        return ResponseEntity.status(HttpStatus.OK).body("S'ha esborrat tots els usuaris");
    }

    // Carrega massiva d'usuaris a través de fitxer CSV
    public int loadFromCSV(String filePath) {
        userLogging.logInfo("Iniciando carga de usuarios desde CSV: " + filePath);

        try {
            int count = userRepository.loadFromCSV(filePath);
            userLogging.logInfo("Carga completada exitosamente. Total de usuarios cargados: " + count);
            return count;
        } catch (IOException e) {
            userLogging.logError("Carga completada exitosamente. Total de usuarios cargados: ", e);
            throw new RuntimeException("Error al cargar el archivo CSV", e);
        }
    }

    // Creacio d'usuari a partir d'un objecte d'usuari
    public User createUser(User user) {
        userLogging.logInfo("Creando nuevo usuario con email: " + user.getEmail());

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            userLogging.logInfo("Error: El email no puede estar vacío");
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            userLogging.logInfo("Error: El password no puede estar vacío");
            throw new IllegalArgumentException("El password no puede estar vacío");
        }

        try {
            userRepository.insertUser(user);
            userLogging.logInfo("Usuario creado exitosamente: " + user.getEmail());
            return user;
        } catch (Exception e) {
            userLogging.logError("Error al crear usuario: ", e);
            throw new RuntimeException("Error al crear el usuario", e);
        }
    }

    // Obtencio de tots els usuaris
    public List<User> getAllUsers() {
        userLogging.logInfo("Obteniendo todos los usuarios");
        try {
            List<User> users = userRepository.findAll();
            userLogging.logInfo("Se encontraron " + users.size() + " usuarios");
            return users;
        } catch (Exception e) {
            userLogging.logError("[cError al obtener usuarios: {}", e);
            throw new RuntimeException("Error al obtener los usuarios", e);
        }
    }

    // Obten un usuari a partir de la seva user id
    public ResponseEntity<String> getUser(long user_id) {
        userLogging.logInfo("Accedint al usuari amb id: " + user_id);

        User user = userRepository.getUserById(user_id);

        if (user == null) {
            userLogging.logError("No existeix cap usuari amb la id: " + user_id, null);
        }

        return ResponseEntity.status(user == null ? HttpStatus.NOT_FOUND : HttpStatus.FOUND)
                .body(user == null ? "No s'ha trobat cap usuari" : "Usuari trobat: " + user);
    }
}