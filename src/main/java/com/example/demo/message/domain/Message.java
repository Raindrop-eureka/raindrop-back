package com.example.demo.message.domain;

import com.example.demo.scene.domain.Scene;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Message {

    private Long messageId;
    private Scene scene;
    private String nickname;
    private String content;
    private String createdAt;
}
