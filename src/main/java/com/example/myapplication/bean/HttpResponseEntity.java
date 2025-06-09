package com.example.myapplication.bean;

import java.io.Serializable;

public class HttpResponseEntity<T> implements Serializable {
    private String code;
    private String message;
    private T data;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
