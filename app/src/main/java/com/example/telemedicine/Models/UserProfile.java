package com.example.telemedicine.Models;

public class UserProfile {
    private String FullName;
    private String Birthday;
    private String Email;
    private String Phone;
    private String Address;
    private String Username;
    private String Base64Photo;
    private String Status;

    public UserProfile(String fullName, String birthday, String email, String phone, String address, String username, String base64Photo, String status) {
        FullName = fullName;
        Birthday = birthday;
        Email = email;
        Phone = phone;
        Address = address;
        Username = username;
        Base64Photo = base64Photo;
        Status = status;
    }

    public String getFullName() {
        return FullName;
    }

    public String getBirthday() {
        return Birthday;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhone() {
        return Phone;
    }

    public String getAddress() {
        return Address;
    }

    public String getUsername() {
        return Username;
    }

    public String getBase64Photo() {
        return Base64Photo;
    }
}
