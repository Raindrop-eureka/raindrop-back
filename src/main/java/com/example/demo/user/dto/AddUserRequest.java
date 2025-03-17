package com.example.demo.user.dto;

import com.example.demo.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor //기본 생성자
@AllArgsConstructor //모든 필드 값을 파라미터로 받는 생성자
@Getter
public class AddUserRequest {
    private String socialId;
    private String name;

    public User toEntity() { //생성자로 객체 생성
        return User.builder()
                .socialId(socialId)
                .name(name)
                .build();
    }
}
