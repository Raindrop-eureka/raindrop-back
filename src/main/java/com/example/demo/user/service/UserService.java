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
    private final KakaoConfig kakaoConfig;
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserMapper userMapper;

    /**
     * 카카오 로그인 - 액세스 토큰 발급
     */
    public KakaoAuthResponse login(KakaoLoginRequest request) {
        String code = request.getCode();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", kakaoConfig.getRedirectUri());
        params.add("client_id", kakaoConfig.getClientId());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                entity,
                KakaoTokenResponse.class
        );

        KakaoTokenResponse tokenResponse = response.getBody();
        if (tokenResponse == null) {
            throw new RuntimeException("카카오 로그인 실패");
        }

        return new KakaoAuthResponse(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
    }

    /**
     * 토큰 갱신
     */
    public KakaoAuthResponse refreshToken(String refreshToken) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refreshToken);
        params.add("client_id", kakaoConfig.getClientId());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                entity,
                KakaoTokenResponse.class
        );

        KakaoTokenResponse tokenResponse = response.getBody();
        if (tokenResponse == null) {
            throw new RuntimeException("토큰 갱신 실패");
        }

        return new KakaoAuthResponse(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
    }

    public UserInfoResponse getUserInfo(String accessToken) {
        // 카카오 API로 사용자 정보 요청
        KakaoInfoResponse kakaoInfoResponse = getKakaoUserInfo(accessToken);

        String socialId = String.valueOf(kakaoInfoResponse.getKakao_account().getEmail());  // 카카오의 이메일을 socialId로 사용
        String nickname = kakaoInfoResponse.getKakao_account().getProfile().getNickname(); // 카카오 사용자 이름
        String profileImageUrl = kakaoInfoResponse.getKakao_account().getProfile().getProfile_image_url(); // 카카오 프로필 이미지 URL

        // DB에서 socialId로 사용자 찾기
        User user = userMapper.findBySocialId(socialId);
        boolean isNewUser = false;

        // 만약 사용자가 없다면 DB에 등록
        if (user == null) {
            User newUser = User.builder()
                    .socialId(socialId)
                    .name(nickname)
                    .build();
            userMapper.saveUser(newUser);  // ✅ JPA save() → MyBatis insert
            isNewUser = true;
        }

        // UserInfoResponse 반환 (이 값은 프론트엔드에 전달)
        return new UserInfoResponse(socialId, nickname, profileImageUrl,isNewUser);
    }

    private KakaoInfoResponse getKakaoUserInfo(String accessToken) {
        // 요청 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 카카오 API 요청
        ResponseEntity<KakaoInfoResponse> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me?secure_resource=true&property_keys=[\"kakao_account.email\",\"kakao_account.profile.nickname\",\"kakao_account.profile.profile_image_url\"]",
                HttpMethod.GET,
                entity,
                KakaoInfoResponse.class
        );

        return response.getBody();
    }
}
