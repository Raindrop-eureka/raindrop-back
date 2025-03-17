package com.example.demo.user.controller;

import com.example.demo.config.KakaoConfig;
import com.example.demo.user.dto.*;
import com.example.demo.user.service.UserService;
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
    private final UserService userService;

    @Operation(summary = "accessToken 발급", description = "code로 accessToken을 발급받아 카카오 로그인 처리")
    @PostMapping("/login")
    public ResponseEntity<KakaoAuthResponse> login(@RequestBody KakaoLoginRequest request) {
        KakaoAuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "token 갱신", description = "refresh-token으로 만료된 access-token과 refresh-token을 갱신해 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<KakaoAuthResponse> getRefreshToken(@RequestHeader("refresh-token") String refreshToken) {
        KakaoAuthResponse response = userService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 정보 조회", description = "accessToken으로 사용자 정보를 조회")
    @GetMapping(path = "/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@RequestHeader("access-token") String accessToken) {
        UserInfoResponse response = userService.getUserInfo(accessToken);
        return ResponseEntity.ok(response);
    }
}