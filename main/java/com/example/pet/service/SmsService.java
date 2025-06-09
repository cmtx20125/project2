package com.example.pet.service;

public interface SmsService {

    boolean sendVerificationCode(String phoneNumber, String code);

}

