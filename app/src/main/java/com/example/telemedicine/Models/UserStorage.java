package com.example.telemedicine.Models;

import java.util.ArrayList;
import java.util.List;

public class UserStorage {
    public StorageToken storageToken;
    public List<UserConsult> consults;

    public UserStorage(StorageToken storageToken) {
        this.storageToken = storageToken;
        consults = new ArrayList<>();
    }

    public void addConsult(UserConsult consult) {
        if (consult != null) {
            consults.add(consult);
        }
    }

    public void setStorageToken(StorageToken storageToken) {
        this.storageToken = storageToken;
    }

    public StorageToken getStorageToken() {
        return storageToken;
    }
}
