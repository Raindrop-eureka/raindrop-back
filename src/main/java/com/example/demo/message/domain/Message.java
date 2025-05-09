package com.example.demo.message.domain;

import com.example.demo.scene.domain.Scene;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Message {

    private Long messageId;
    private Scene scene; // Scene 객체
    private String nickname;
    private String content;
    private String modelId; // 3D 모델과 매핑을 위한 필드 추가
    private LocalDateTime createdAt;

    @Builder
    public Message(Scene scene, String nickname, String content, String modelId, LocalDateTime createdAt) {
        this.scene = scene;
        this.nickname = nickname;
        this.content = content;
        this.modelId = modelId;
        this.createdAt = createdAt;
    }

    public Long getSceneId() {
        return (scene != null) ? scene.getSceneId() : null;
    }
}