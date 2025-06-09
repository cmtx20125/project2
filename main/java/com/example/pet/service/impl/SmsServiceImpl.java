package com.example.pet.service.impl;

import com.example.pet.config.AliyunSmsConfig;
import com.example.pet.service.SmsService;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Author YangLi
 * @Date 2024/9/11 18:21
 * @注释
 */
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Autowired
    private AliyunSmsConfig aliyunSmsConfig;


    @Override
    public boolean sendVerificationCode(String phoneNumber, String code) {
        try {
            // aliyun 短信测试控制台地址  https://dysms.console.aliyun.com/quickstart?spm=5176.25163407.domtextsigncreate-index-1ec3c_58c50_0.1.3ac1bb6eYTArb1
            // 因为是测试，这个地址先绑定测试机号码，选择专用测试模版 然后点击 下方调用api发送消息  就能看到 signName, templateCode,templateParam 这4个参数
            // 如果是正式环境 需要先审核资质，审核模板，然后添加签名，审核完成之后，就能看到所需要的参数（这里我自己为了方便就不去做资质审核了）
            SendSmsResponse response = aliyunSmsConfig.sendSms(
                    phoneNumber,
                    "阿里云短信测试", // 阿里云短信签名
                    "SMS_154950909", // 阿里云短信模板代码
                    "{\"code\":\"" + code + "\"}" // 模板参数
            );
            return "OK".equals(response.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
