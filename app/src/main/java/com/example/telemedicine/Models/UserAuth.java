package com.example.telemedicine.Models;

public class UserAuth {
    private String Email;
    private String Password;

    public UserAuth(String email, String password) {
        Email = email;
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }
}
