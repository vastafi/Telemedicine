package com.example.telemedicine.Utils;

import android.content.Context;

import com.example.telemedicine.Models.StorageToken;
import com.example.telemedicine.Models.UserConsult;
import com.example.telemedicine.Models.UserStorage;
import com.example.telemedicine.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Scanner;

public class Storage {

    private Context context;
    private String filename;
    private Gson gson;
    private final int expirationMinutes = 60;

    public Storage(Context context) {
        this.context = context;
        this.filename = context.getString(R.string.default_storage_file);
        this.gson = new GsonBuilder().create();
    }

    public UserStorage getUserStorage() throws Exception {
        initFileIfNotExist();

        File file = getFile();
        StringBuilder content = new StringBuilder((int) file.length());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine() + System.lineSeparator());
            }
        }

        UserStorage userStorage = gson.fromJson(content.toString(), new TypeToken<UserStorage>() {
        }.getType());
        return userStorage;
    }

    public void updateToken(String tokenString) throws Exception {
        initFileIfNotExist();

        if (tokenString.length() == 0) {
            return;
        }
        UserStorage userStorage = getUserStorage();
        StorageToken tokenObject = new StorageToken(tokenString, DateUtil.getExpiredDate(new Date(), expirationMinutes));
        userStorage.setStorageToken(tokenObject);

        writeToFile(userStorage);
    }

    public void deleteToken() throws Exception {
        initFileIfNotExist();

        UserStorage userStorage = getUserStorage();
        StorageToken tokenObject = new StorageToken("", new Date());
        userStorage.setStorageToken(tokenObject);

        writeToFile(userStorage);
    }

    public boolean validateToken() {
        try {
            StorageToken tokenObject = getUserStorage().getStorageToken();
            if (tokenObject.getToken().length() > 0) {
                if (!DateUtil.isExpired(tokenObject.getDate(), new Date())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addConsult(UserConsult consult) throws Exception {
        initFileIfNotExist();

        if (consult == null) {
            return;
        }
        UserStorage userStorage = getUserStorage();
        userStorage.addConsult(consult);

        writeToFile(userStorage);
    }

    public void updateConsultConfirmation(UserConsult userConsult) throws Exception {
        initFileIfNotExist();

        UserStorage userStorage = getUserStorage();

        if (userStorage.consults.removeIf(c -> c.getConsId() == userConsult.getConsId())) {
            userConsult.setConfirmed(true);
            userStorage.consults.add(userConsult);

            writeToFile(userStorage);
        }
    }

    public void removeConsult(int id) throws Exception {
        initFileIfNotExist();

        UserStorage userStorage = getUserStorage();
        userStorage.consults.removeIf(c -> c.getConsId() == id);

        writeToFile(userStorage);
    }

    public UserConsult getLastUnconfirmedConsult() throws Exception {
        initFileIfNotExist();

        UserStorage userStorage = getUserStorage();

        return userStorage.consults.stream()
                .filter(c -> !c.isConfirmed())
                .findAny()
                .orElse(null);
    }

    private void initFileIfNotExist() throws Exception {
        File file = getFile();
        if (!file.exists()) {
            file.createNewFile();
            initUserStorage();
        }
    }

    private void initUserStorage() throws Exception {
        StorageToken tokenObject = new StorageToken("", new Date());
        UserStorage userStorage = new UserStorage(tokenObject);

        writeToFile(userStorage);
    }

    private void writeToFile(UserStorage userStorage) throws Exception {
        FileWriter writer = new FileWriter(getFile());
        String jsonString = gson.toJson(userStorage);
        writer.write(jsonString);
        writer.flush();
        writer.close();
    }

    private File getFile() {
        return new File(context.getFilesDir(), filename);
    }
}
