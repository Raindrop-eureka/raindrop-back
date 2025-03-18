package com.example.demo.scene.domain;

import com.example.demo.location.domain.Location;
import com.example.demo.user.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Scene {

    private Long sceneId;
    private User user;  // 연관 관계는 매핑 파일에서 처리
    private String theme;
    private Location location;

    @JsonProperty("isVisible")
    private boolean isVisible = false;

    public Scene(Long sceneId, User user, String theme, Location location, boolean isVisible) {
        this.sceneId = sceneId;
        this.user = user;
        this.theme = theme;
        this.location = location;
        this.isVisible = isVisible;
    }
}
