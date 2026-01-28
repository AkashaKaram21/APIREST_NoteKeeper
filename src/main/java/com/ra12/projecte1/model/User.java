package com.ra12.projecte1.model;

public class User {
    private String email;
    private String password;

    // Constructor vacío
    public User() {
    }

    // Constructor completo
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters y Setters
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
        return "User{" +
                "email='" + email + '\'' +
                ", password='***'" +
                '}';
    }
}