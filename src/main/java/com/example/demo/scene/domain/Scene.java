package com.example.demo.scene.domain;

import com.example.demo.user.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Scene {

    private Long sceneId;
    private User user;
    private String theme;

    @JsonProperty("isMessageVisible")
    private boolean isMessageVisible = false;

    @Builder
    public Scene(Long sceneId, User user, String theme, boolean isMessageVisible) {
        this.sceneId = sceneId;
        this.user = user;
        this.theme = theme;
        this.isMessageVisible = isMessageVisible;
    }
}
