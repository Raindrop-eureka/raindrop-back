package com.example.demo.scene.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SceneCreateRequest {

    private String socialId;
    private String theme;
    private Double latitude;
    private Double longitude;
}
