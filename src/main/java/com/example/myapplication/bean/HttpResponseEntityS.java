package com.example.myapplication.bean;

public class HttpResponseEntityS {
    private String code;
    private User data;
    private String message;

    // Getter 和 Setter 方法
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public User getData() { return data; }
    public void setData(User data) { this.data = data; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
