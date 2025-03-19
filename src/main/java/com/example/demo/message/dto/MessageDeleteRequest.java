package com.example.demo.message.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDeleteRequest {
    private Long sceneId;
    private Long messageId;
}