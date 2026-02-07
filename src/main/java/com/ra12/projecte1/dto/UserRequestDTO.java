package com.ra12.projecte1.dto;

// L'email i contrasenya per fer una request d'un usuari
// a una login page per exemple introduiriem l'email i la password
// per accedir a l'informació de l'usuari
public class UserRequestDTO {
    private String email;
    private String password;

    public UserRequestDTO() {

    }

    public UserRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserRequestDTO [email=" + email + ", password=" + password + "]";
    }
}