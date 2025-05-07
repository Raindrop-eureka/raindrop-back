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
    private String profileImageUrl;

    @Builder
    public User(String socialId, String name,  String profileImageUrl) {
        this.socialId = socialId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }
}
