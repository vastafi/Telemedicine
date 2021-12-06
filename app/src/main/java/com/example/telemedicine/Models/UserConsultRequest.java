package com.example.telemedicine.Models;

public class UserConsultRequest {
    private String Name;
    private String Disease;
    private String Address;
    private String Description;

    public UserConsultRequest(String name, String disease, String address, String description) {
        Name = name;
        Disease = disease;
        Address = address;
        Description = description;
    }
}
