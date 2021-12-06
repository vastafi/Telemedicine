package com.example.telemedicine.Models;

public class UserRegister {
    private String FullName;
    private String Birthday;
    private String Email;
    private String Phone;
    private String Address;
    private String Username;
    private String Password;
    private String Base64Photo;

    public UserRegister(String fullName, String username, String password, String birthday, String email, String phone, String address, String base64Photo) {
        FullName = fullName;
        Username = username;
        Password = password;
        Birthday = birthday;
        Email = email;
        Phone = phone;
        Address = address;
        Base64Photo = base64Photo;
    }
}
