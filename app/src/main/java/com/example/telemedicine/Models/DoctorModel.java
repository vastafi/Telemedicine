package com.example.telemedicine.Models;

public class DoctorModel {
    private int DocId;
    private String FullName;
    private String Specs;
    private String Address;
    private String About;
    private double Stars;
    private String Photo;

    public DoctorModel() {
    }

    public DoctorModel(int docId, String fullName, String specs, String address, String about, double stars, String photo) {
        this.DocId = docId;
        this.FullName = fullName;
        this.Specs = specs;
        this.Address = address;
        this.About = about;
        this.Stars = stars;
        this.Photo = photo;
    }

    public int getDocId() {
        return DocId;
    }

    public String getFullName() {
        return FullName;
    }
    public String getSpecs() {
        return Specs;
    }

    public String getAddress() {
        return Address;
    }

    public String getAbout() {
        return About;
    }

    public double getStars() {
        return Stars;
    }

    public String getPhoto() {
        return Photo;
    }
}
