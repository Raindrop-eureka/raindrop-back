package com.example.demo.user.controller;

import com.example.demo.common.dto.ApiResponse;
import com.example.demo.user.dto.*;
import com.example.demo.user.service.KakaoAuthService;
import com.example.demo.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name="User",description = "사용자 정보")
public class UserController {
    private final UserService userService;
    private final KakaoAuthService kakaoAuthService;

    @Operation(summary = "accessToken 발급", description = "code로 accessToken을 발급받아 카카오 로그인 처리")
    @PostMapping("/login")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "400에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "401에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "403에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "404에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<LoginResponse> login(@RequestBody KakaoLoginRequest request) {
        // 기존 로직으로 토큰 얻기
        KakaoAuthResponse authResponse = kakaoAuthService.getAccessToken(request.getCode());

        // HTTP Only 쿠키로 accessToken 설정
        ResponseCookie accessTokenCookie = ResponseCookie.from("access-token", authResponse.getAccessToken())
                .httpOnly(true)             // JavaScript에서 접근 불가능
                .secure(true)               // HTTPS에서만 전송
                .sameSite("None")           // 크로스 사이트 요청 허용
                .path("/")                  // 모든 경로에서 사용 가능
                .maxAge(3600)               // 1시간 유효
                .domain("raindrop-front.vercel.app")
                .build();

        // HTTP Only 쿠키로 refreshToken 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh-token", authResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(2592000)           // 30일 유효
                .domain("raindrop-front.vercel.app")
                .build();

        // 클라이언트에게 성공 메시지만 반환하고 토큰은 쿠키로 전송
        LoginResponse response = new LoginResponse("로그인 성공", true);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(response);
    }

    @Operation(summary = "token 갱신", description = "refresh-token으로 만료된 access-token과 refresh-token을 갱신해 재발급")
    @PostMapping("/refresh")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "400에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "401에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "403에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "404에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<LoginResponse> refresh(@CookieValue(name = "refresh-token", required = false) String refreshToken) {
        // 쿠키에서 리프레시 토큰을 받아옴
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("리프레시 토큰이 없습니다", false));
        }

        // 기존 로직으로 토큰 갱신
        KakaoAuthResponse authResponse = kakaoAuthService.refreshToken(refreshToken);

        // HTTP Only 쿠키로 accessToken 설정
        ResponseCookie accessTokenCookie = ResponseCookie.from("access-token", authResponse.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(3600)
                .build();

        // HTTP Only 쿠키로 refreshToken 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh-token", authResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(2592000)
                .build();

        // 클라이언트에게 성공 메시지만 반환하고 토큰은 쿠키로 전송
        LoginResponse response = new LoginResponse("토큰 갱신 성공", true);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(response);
    }

    @Operation(summary = "사용자 정보 조회", description = "accessToken으로 사용자 정보를 조회")
    @GetMapping(path = "/info")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "400에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "401에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "403에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "404에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<UserInfoResponse> getUserInfo(@CookieValue(name = "access-token", required = false) String accessToken) {
        // 쿠키에서 액세스 토큰을 받아옴
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserInfoResponse response = userService.getUserInfo(accessToken);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃 처리")
    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout() {
        // 쿠키 삭제를 위해 만료 시간을 0으로 설정
        ResponseCookie accessTokenCookie = ResponseCookie.from("access-token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh-token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new LoginResponse("로그아웃 성공", true));
    }
}

// 로그인 응답 클래스 추가
class LoginResponse {
    private String message;
    private boolean success;

    public LoginResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}