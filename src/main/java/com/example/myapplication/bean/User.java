package com.example.myapplication.bean;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    String userId;
    String userName;
    String userPhone;
    String userPic;
     String userAddress;
     String userGender;
    Date userDate;

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

    public User(String userId, String userName, String userPhone, String userPic, String userPwd, Date userTime) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userPic = userPic;
        this.userPwd = userPwd;
        this.userTime = userTime;
    }
    String userPwd;
    Date userTime;
    int userTago;

    public int getUserTago() {
        return userTago;
    }

    public void setUserTago(int userTago) {
        this.userTago = userTago;
    }

    public int getUserTagt() {
        return userTagt;
    }

    public void setUserTagt(int userTagt) {
        this.userTagt = userTagt;
    }

    public int getUserTagtr() {
        return userTagtr;
    }

    public void setUserTagtr(int userTagtr) {
        this.userTagtr = userTagtr;
    }

    int userTagt;
    int userTagtr;
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public Date getUserTime() {
        return userTime;
    }

    public void setUserTime(Date userTime) {
        this.userTime = userTime;
    }
    public User(){}
}
