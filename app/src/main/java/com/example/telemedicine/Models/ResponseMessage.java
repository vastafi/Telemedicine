package com.example.telemedicine.Models;

public class ResponseMessage {
    public String Status;
    public String Message;

    public ResponseMessage(String status, String message) {
        Status = status;
        Message = message;
    }
}
