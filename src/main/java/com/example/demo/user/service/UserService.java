package com.example.demo.user.service;

import com.example.demo.config.KakaoConfig;
import com.example.demo.user.domain.User;
import com.example.demo.user.dto.*;
import com.example.demo.user.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final KakaoAuthService kakaoAuthService;  // ✅ KakaoAuthService 주입

    public UserInfoResponse getUserInfo(String accessToken) {
        KakaoInfoResponse kakaoInfoResponse = kakaoAuthService.getUserInfo(accessToken);  // ✅ KakaoAuthService 사용

        String socialId = kakaoInfoResponse.getKakao_account().getEmail();
        String nickname = kakaoInfoResponse.getKakao_account().getProfile().getNickname();
        String profileImageUrl = kakaoInfoResponse.getKakao_account().getProfile().getProfile_image_url();

        User user = userMapper.findBySocialId(socialId);
        boolean isNewUser = false;

        if (user == null) {
            User newUser = User.builder()
                    .socialId(socialId)
                    .name(nickname)
                    .build();
            userMapper.saveUser(newUser);
            isNewUser = true;
        }

        return new UserInfoResponse(socialId, nickname, profileImageUrl, isNewUser);
    }
}