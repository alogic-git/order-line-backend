package com.orderline.basic.service;

import com.orderline.basic.exception.ExternalServerErrorException;
import com.orderline.basic.model.dto.SmsDto;
import com.orderline.basic.config.Env;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


@DependsOn("Env")
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {
    @Resource(name = "restTemplate")
    private final RestTemplate restTemplate;

    @Resource(name = "mailSender")
    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String from;

    private static final String SMS_API_KEY = Env.smsApiKey();
    private static final String API_ACCESS_KEY = Env.smsAccessKey();
    private static final String API_SECRET_KEY = Env.smsSecretKey();
    private static final String SMS_URI = String.format("/sms/v2/services/%s/messages", SMS_API_KEY);
    private static final String SMS_URL = String.format("https://sens.apigw.ntruss.com%s", SMS_URI);
    private static final String SMS_PHONE_NUMBER = Env.smsPhone();

    // 문자 발송 API
    public Integer sendSms(String phone, String messageContent) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("x-ncp-apigw-timestamp", timestamp);
        headers.set("x-ncp-iam-access-key", API_ACCESS_KEY);
        headers.set("x-ncp-apigw-signature-v2", createSignature(SMS_URI, timestamp)); // Secret Key 암호화

        List<SmsDto.MessagesDto> messages = new ArrayList<>();
        messages.add(SmsDto.MessagesDto.builder()
                .to(phone)
                .content(messageContent)
                .build());

        SmsDto.SmsRequestDto request = SmsDto.SmsRequestDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(SMS_PHONE_NUMBER)
                .content(messageContent)
                .messages(messages)
                .build();

        HttpEntity<SmsDto.SmsRequestDto> requestHttpEntity = new HttpEntity<>(request, headers);
        try {
            SmsDto.SmsResponseDto response = restTemplate.postForObject(SMS_URL, requestHttpEntity, SmsDto.SmsResponseDto.class);
            if(response == null){
                return HttpStatus.INTERNAL_SERVER_ERROR.value();
            }
            return response.getStatusCode();
        } catch (RestClientException e) {
            e.printStackTrace();
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }

    public String createSignature(String uri, String timestamp) {
        String message = "POST" +
                " " +
                uri +
                "\n" +
                timestamp +
                "\n" +
                API_ACCESS_KEY;

        byte[] bytes;
        bytes = API_SECRET_KEY.getBytes(StandardCharsets.UTF_8);

        SecretKeySpec signingKey = new SecretKeySpec(bytes, "HmacSHA256");

        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
        }
        catch (NoSuchAlgorithmException e) {
            throw new ExternalServerErrorException("네이버 아이디 로그인중 오류가 발생했습니다.[1]");
        }

        try {
            mac.init(signingKey);
        }
        catch (InvalidKeyException e) {
            throw new ExternalServerErrorException("네이버 아이디 로그인중 오류가 발생했습니다.[2]");
        }

        byte[] rawHmac;
        rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeBase64String(rawHmac);
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);  // Set the sender address
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
