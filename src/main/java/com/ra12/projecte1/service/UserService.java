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
import com.ra12.projecte1.model.User;
import com.ra12.projecte1.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<String> setUserImage(UserRequestDTO userRequest, MultipartFile imageFile) throws Exception {
        //customLogging.logInfo("Afegint la imatge " + (imageFile != null ? imageFile.getName() : "null") + " per l'usuari amb id: " + user_id);

        User user = userRepository.getUserByUserRequestDTO(userRequest);

        if (user == null) {
            //customLogging.logError("L'usuari amb id: " + user_id + " no existeix", null);

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No s'ha pogut trobar l'usuari.");
        }

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

        //customLogging.logInfo("La imatge s'ha guardat correctament. El path és: " + imagePath);

        return ResponseEntity.status(HttpStatus.OK)
                .body("S'ha pujat la foto de l'usuari amb id: " + user.getId() + " al path: " + imagePath);
    }
}
