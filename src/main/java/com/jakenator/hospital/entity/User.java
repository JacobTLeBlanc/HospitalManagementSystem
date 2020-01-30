package com.jakenator.hospital.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private String email;

    private String password;

    // Is user a patient, doctor or manager
    private String userType;

    // Id to find information on user
    private int userId;

    public User() {}

    public User(String email, String password, String userType, int userId) {
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }

    public int getUserId() {
        return userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
