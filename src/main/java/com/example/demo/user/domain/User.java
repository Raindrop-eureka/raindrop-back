package com.example.demo.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private String socialId;
    private String name;

    @Builder
    public User(String socialId, String name) {
        this.socialId = socialId;
        this.name = name;
    }
}
