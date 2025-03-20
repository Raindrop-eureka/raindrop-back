package com.example.demo.scene.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SceneDto {
    private Long sceneId;
    private String socialId;
    private String theme;
    private boolean isMessageVisible;
}
