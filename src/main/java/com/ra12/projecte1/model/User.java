package com.ra12.projecte1.model;

import java.sql.Timestamp;

public class User {
    
    long id;
    String name;
    String email;
    String password;
    Timestamp ultimAcces;
    Timestamp dataCreated;
    Timestamp dataUpdated;

    // Constructor vacío necesario
    public User() {
    }

    // Constructor con parámetro
    public User(Timestamp dataUpdated) {
        this.dataUpdated = dataUpdated;
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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Timestamp getUltimAcces() {
        return ultimAcces;
    }
    public void setUltimAcces(Timestamp ultimAcces) {
        this.ultimAcces = ultimAcces;
    }
    public Timestamp getDataCreated() {
        return dataCreated;
    }
    public void setDataCreated(Timestamp dataCreated) {
        this.dataCreated = dataCreated;
    }
    public Timestamp getDataUpdated() {
        return dataUpdated;
    }
    public void setDataUpdated(Timestamp dataUpdated) {
        this.dataUpdated = dataUpdated;
    }
}