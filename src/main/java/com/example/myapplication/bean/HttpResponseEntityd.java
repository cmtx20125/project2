package com.example.myapplication.bean;

public class HttpResponseEntityd {
    private String code;
    private Animal data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Animal getData() {
        return data;
    }

    public void setData(Animal data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
