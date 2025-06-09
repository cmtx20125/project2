package com.example.myapplication.bean;

import java.util.Date;

public class DialogueDto {
    String userId;
    String userName;
    String userPic;
    String userAddress;
    String userGender;
    Date userDate;
    String userIdNow;
    int userTagt;

    public int getUserTagt() {
        return userTagt;
    }

    public void setUserTagt(int userTagt) {
        this.userTagt = userTagt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public Date getUserDate() {
        return userDate;
    }

    public void setUserDate(Date userDate) {
        this.userDate = userDate;
    }

    public String getUserIdNow() {
        return userIdNow;
    }

    public void setUserIdNow(String userIdNow) {
        this.userIdNow = userIdNow;
    }
}
