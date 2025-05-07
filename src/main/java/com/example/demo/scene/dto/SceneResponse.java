package com.example.demo.scene.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SceneResponse {
    private Long sceneId;
    private String theme;

    @JsonProperty("isMessageVisible")
    private boolean isMessageVisible;
    private String ownerSocialId;

    private String ownerNickname;
    private String ownerProfileImage;
}
