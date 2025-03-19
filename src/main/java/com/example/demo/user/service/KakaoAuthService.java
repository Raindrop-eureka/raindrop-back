package com.example.demo.user.service;

import com.example.demo.user.dto.KakaoAuthResponse;
import com.example.demo.user.dto.KakaoInfoResponse;
import com.example.demo.user.dto.KakaoTokenResponse;
import com.example.demo.config.KakaoConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    private final KakaoConfig kakaoConfig;
    private final RestTemplate restTemplate;

    /**
     * 카카오 로그인 - 액세스 토큰 발급
     */
    public KakaoAuthResponse getAccessToken(String code) {
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

    /**
     * Kakao 사용자 정보 조회
     */
    public KakaoInfoResponse getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoInfoResponse> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me?secure_resource=true&property_keys=[\"kakao_account.email\",\"kakao_account.profile.nickname\",\"kakao_account.profile.profile_image_url\"]",
                HttpMethod.GET,
                entity,
                KakaoInfoResponse.class
        );

        return response.getBody();
    }
}
