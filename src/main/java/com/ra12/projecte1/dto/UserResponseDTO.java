package com.ra12.projecte1.dto;

public class UserResponseDTO {
    private String email;

    public UserResponseDTO() {
    }

    public UserResponseDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}