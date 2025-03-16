package com.example.demo.scene.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SceneUpdateVisibilityRequest {
    @JsonProperty("isVisible")
    private boolean isVisible;
}
