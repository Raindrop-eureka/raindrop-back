package com.example.demo.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {
    private String email;
    private String nickname;
    private String profileImageUrl;
    private boolean isNewUser;  // 신규 유저 여부

    public UserInfoResponse(String email, String nickname, String profileImageUrl, boolean isNewUser) {
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.isNewUser = isNewUser;
    }
}
