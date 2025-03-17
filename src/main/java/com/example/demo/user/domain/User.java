package com.example.demo.user.domain;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @Column(name = "social_id", nullable = false, unique = true)
    private String socialId;

    @Column(name = "name", nullable = false)
    private String name;

    @Builder //객체 생성
    public User(String socialId, String name) {
        this.socialId = socialId;
        this.name = name;
    }
}
