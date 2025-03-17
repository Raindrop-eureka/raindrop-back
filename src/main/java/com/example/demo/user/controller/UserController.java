package com.example.demo.user.controller;

import com.example.demo.user.dto.KakaoLoginRequest;
import com.example.demo.user.dto.KakaoTokenResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j // Lombok을 이용한 로깅
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name="User",description = "사용자 정보")
public class UserController {
    @PostMapping("/login")
    public ResponseEntity<KakaoTokenResponse>  login(@RequestBody KakaoLoginRequest request){
        log.info("Received Kakao login request: {}", request.getCode());
        String code = request.getCode(); // JSON에서 code 값 추출

        //Body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("code",code);
        params.add("redirect_uri","http://localhost:5173/auth/login/kakao");
        params.add("client_id","8162b95c200bcd82ce88d8c5468f41c5");

        log.info("Generated request body: {}", params); // Body 확인

        //Header생성
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

        log.info("Generated headers: {}", headers); // Header 확인

        //요청 객체 생성
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        log.info("Final request entity: {}", entity); // 최종 요청 객체 확인

        //Post요청 보내기
        RestTemplate rt = new RestTemplate();

        ResponseEntity<KakaoTokenResponse> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token", //{요청할 서버 주소}
                HttpMethod.POST, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                KakaoTokenResponse.class // 🔹 DTO 클래스로 응답 받기
        );

        log.info("Response from Kakao: {}", response.getBody()); // 응답 값 확인

        return response;
    }

    @GetMapping("/info")
    public String getUserInfo(){
        return "user info";
    }
}
