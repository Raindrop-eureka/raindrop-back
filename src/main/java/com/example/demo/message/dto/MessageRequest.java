package com.example.demo.message.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {
    private Long sceneId;
    private String nickname;
    private String content;
}
