package com.example.telemedicine.Models;

import java.util.Date;

public class StorageToken {
    private String token;
    private Date date;

    public StorageToken() {}

    public StorageToken(String token, Date date) {
        this.token = token;
        this.date = date;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Date getDate() {
        return date;
    }
}
