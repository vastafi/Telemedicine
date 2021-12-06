package com.example.telemedicine.Models;

public class UserConsult {
    private int ConsId;
    private String Name;
    private String Disease;
    private String Address;
    private String Description;
    private int DocId;
    private boolean IsConfirmed;

    public UserConsult(int consId, String name, String disease, String address, String description, int docId, boolean isConfirmed) {
        ConsId = consId;
        Name = name;
        Disease = disease;
        Address = address;
        Description = description;
        DocId = docId;
        IsConfirmed = isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        IsConfirmed = confirmed;
    }

    public int getConsId() {
        return ConsId;
    }

    public String getName() {
        return Name;
    }

    public String getDisease() {
        return Disease;
    }

    public String getAddress() {
        return Address;
    }

    public String getDescription() {
        return Description;
    }

    public int getDocId() {
        return DocId;
    }

    public boolean isConfirmed() {
        return IsConfirmed;
    }
}
