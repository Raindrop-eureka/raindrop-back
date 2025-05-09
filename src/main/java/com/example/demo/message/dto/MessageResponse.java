package com.example.demo.message.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageResponse {

    private Long messageId;
    private String nickname;
    private String content;
    private String modelId; // 3D 모델 ID 추가
    private LocalDateTime createdAt;

    // Constructor for the response DTO
    public MessageResponse(Long messageId, String nickname, String content, String modelId, LocalDateTime createdAt) {
        this.messageId = messageId;
        this.nickname = nickname;
        this.content = content;
        this.modelId = modelId;
        this.createdAt = createdAt;
    }
}