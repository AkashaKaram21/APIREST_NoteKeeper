package com.ra12.projecte1.dto;

import com.ra12.projecte1.model.User;

// Tota l'informació sobre un usuari que podem fer display sense
// filtrar la contrasenya, la qual s'asumeix es privada
public class UserResponseDTO {
    private long id;
    private String name;
    private String email;
    private String imagePath;

    public UserResponseDTO() {
        
    }

    public UserResponseDTO(long id, String name, String email, String imagePath) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imagePath = imagePath;
    }

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.imagePath = user.getImagePath();
    }
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "UserResponseDTO [id=" + id + ", name=" + name + ", email=" + email + ", imagePath=" + imagePath + "]";
    }
}