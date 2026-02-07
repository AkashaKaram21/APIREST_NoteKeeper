package com.ra12.projecte1.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

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

    // Funcio de debug perque no tinc el codi d'afegir / obtenir usuaris
    public ResponseEntity<String> createUser(User user) {
        userLogging.logInfo("Creant un usuari");

        try {
            userRepository.insertUser(user);
        } catch (Exception exception) {
            userLogging.logError("L'usuari amb nom: " + user.getName() + " no s'ha creat correctament", exception);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No s'ha pogut crear l'Usuari: \"" + user + "\", error: " + exception.getLocalizedMessage());
        }

        userLogging.logInfo("Usuari creat correctament");

        return ResponseEntity.status(HttpStatus.OK).body("Afegit l'usuari " + new UserResponseDTO(user));
    }

    // A través del userRequest (email i contrasenya) asignem una imatge i la guardem
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
    // Si l'usuari existeix i els canvis no son nulls i són diferents als valors actuals, es cambiaran
    public ResponseEntity<String> updateUser(long userId, User updatedUser) throws Exception {

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
}