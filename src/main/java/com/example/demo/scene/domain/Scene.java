package com.example.demo.scene.domain;

import com.example.demo.user.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Scene {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sceneId;

    @OneToOne
    @JoinColumn(name = "social_id", referencedColumnName = "social_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String theme;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    @JsonProperty("isVisible")
    private boolean isVisible = false;
}
