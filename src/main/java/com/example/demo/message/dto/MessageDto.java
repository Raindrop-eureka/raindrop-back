package com.example.demo.message.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDto {
    private Long messageId;
    private Long sceneId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
}
