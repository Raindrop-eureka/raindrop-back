package com.example.demo.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {
    private String email;
    private String nickname;
    private String profileImageUrl;

    public UserInfoResponse(String email, String nickname, String profileImageUrl) {
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
