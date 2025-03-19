package com.example.demo.utils;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class AESUtil {
    private final AES256TextEncryptor encryptor;

    public AESUtil(@Value("${jasypt.encryptor.password}") String secretKey) {
        this.encryptor = new AES256TextEncryptor();
        this.encryptor.setPassword(secretKey);
    }

    // 암호화
    public String encrypt(String plainText) {
        String encryptedText = encryptor.encrypt(plainText);
        // Base64 URL-safe 인코딩
        return Base64.getUrlEncoder().encodeToString(encryptedText.getBytes());
    }

    // 복호화
    public String decrypt(String encryptedText) {
        // Base64 URL-safe 디코딩
        String decodedText = new String(Base64.getUrlDecoder().decode(encryptedText));
        return encryptor.decrypt(decodedText);
    }

    // 암호화된 sceneId를 복호화하는 메서드
    public Long decryptSceneId(String encryptedSceneId) {
        try {
            String decryptedId = decrypt(encryptedSceneId);  // 암호화된 sceneId 복호화
            return Long.parseLong(decryptedId);
        } catch (Exception e) {
            throw new IllegalArgumentException("잘못된 접근입니다. 유효하지 않은 게시판 ID입니다.");
        }
    }
}