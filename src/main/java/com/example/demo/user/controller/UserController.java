package com.example.demo.user.controller;

import com.example.demo.config.KakaoConfig;
import com.example.demo.user.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j // Lombok을 이용한 로깅
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name="User",description = "사용자 정보")
public class UserController {
    private final KakaoConfig kakaoConfig;

    @Operation(summary = "accessToken 발급", description = "code로 accessToken을 발급받아 카카오 로그인 처리")
    @PostMapping("/login")
    public ResponseEntity<KakaoAuthResponse> login(@RequestBody KakaoLoginRequest request) {
        String code = request.getCode(); // JSON에서 code 값 추출

        // Body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", kakaoConfig.getRedirectUri());
        params.add("client_id", kakaoConfig.getClientId());

        // Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

        // 요청 객체 생성
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        // Post 요청 보내기
        RestTemplate rt = new RestTemplate();
        ResponseEntity<KakaoTokenResponse> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                entity,
                KakaoTokenResponse.class
        );

        KakaoTokenResponse tokenResponse = response.getBody();
        if (tokenResponse == null) {
            log.error("Failed to get response from Kakao");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // 필요한 값만 포함하여 반환
        KakaoAuthResponse authResponse = new KakaoAuthResponse(
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken()
        );

        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "token 갱신", description = "refresh-token으로 만료된 access-token과 refresh-token을 갱신해 재발급")
    @PostMapping(path = "/refresh")
    public ResponseEntity<KakaoAuthResponse> getRefreshToken(@RequestHeader("refresh-token") String refreshToken) {
        //Body생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refreshToken);
        params.add("client_id", kakaoConfig.getClientId());

        //Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        // Post 요청 보내기
        RestTemplate rt = new RestTemplate();
        ResponseEntity<KakaoTokenResponse> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                entity,
                KakaoTokenResponse.class
        );

        KakaoTokenResponse tokenResponse = response.getBody();
        if (tokenResponse == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        log.info("Response from Kakao: {}", tokenResponse);

        // 필요한 값만 포함하여 반환
        KakaoAuthResponse authResponse = new KakaoAuthResponse(
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken()
        );

        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "사용자 정보 조회", description = "accessToken으로 사용자 정보를 조회")
    @GetMapping(path = "/info")
    public UserInfoResponse getUserInfo(@RequestHeader("access-token") String accessToken ){
        // Body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        // Get 요청 보내기
        RestTemplate rt = new RestTemplate();
        ResponseEntity<KakaoInfoResponse> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me?secure_resource=true&property_keys=[\"kakao_account.email\",\"kakao_account.profile.nickname\",\"kakao_account.profile.profile_image_url\"]",
                HttpMethod.GET,
                entity,
                KakaoInfoResponse.class
        );
        // 응답에서 필요한 값만 추출
        KakaoInfoResponse kakaoInfoResponse = response.getBody();
        String email = kakaoInfoResponse.getKakao_account().getEmail();
        String nickname = kakaoInfoResponse.getKakao_account().getProfile().getNickname();
        String profileImageUrl = kakaoInfoResponse.getKakao_account().getProfile().getProfile_image_url();

        // DTO 객체로 반환
        return new UserInfoResponse(email, nickname, profileImageUrl);
    }
}
