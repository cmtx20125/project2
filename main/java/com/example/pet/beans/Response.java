package com.example.pet.beans;

import org.springframework.stereotype.Repository;

@Repository
public class Response {
    private boolean success;
    private String message;
    public Response(){

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
