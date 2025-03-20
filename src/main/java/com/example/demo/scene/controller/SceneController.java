package com.example.demo.scene.controller;

import com.example.demo.scene.dto.SceneRequest;
import com.example.demo.scene.dto.SceneResponse;
import com.example.demo.scene.dto.SceneUpdateVisibilityRequest;
import com.example.demo.scene.service.SceneService;
import com.example.demo.user.service.KakaoAuthService;
import com.example.demo.utils.AESUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scenes")
@Tag(name="Scene", description="Scene 테이블 관련 API")
public class SceneController {

    private final SceneService sceneService;
    private final AESUtil aesUtil;
    private final KakaoAuthService kakaoAuthService;

    @Operation(summary = "Scene 생성", description = "액세스 토큰을 통해 Scene 생성 후 암호화된 SceneId 반환")
    @PostMapping
    public ResponseEntity<String> createScene(
            @RequestHeader("access-token") String accessToken,
            @RequestBody SceneRequest request) {

        Long sceneId = sceneService.createScene(accessToken, request);
        String encryptedSceneId = aesUtil.encrypt(String.valueOf(sceneId));
        return ResponseEntity.status(HttpStatus.CREATED).body(encryptedSceneId);
    }

    @Operation(summary = "Scene 수정 (테마 수정)", description = "액세스 토큰을 통해 사용자 인증 후 Scene 테마 수정")
    @PutMapping("/theme")  // 암호화된 sceneId
    public ResponseEntity<Void> updateScene(
            @RequestHeader("access-token") String accessToken,
            @RequestBody SceneRequest request) {
        sceneService.updateScene(accessToken, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Scene 메시지 공개 상태 수정", description = "액세스 토큰을 통해 사용자 인증 후 Scene의 메시지 공개 여부를 수정")
    @PutMapping("/visibility")  // 암호화된 sceneId
    public ResponseEntity<Void> updateVisibility(
            @RequestHeader("access-token") String accessToken,
            @RequestBody SceneUpdateVisibilityRequest request) {
        sceneService.updateVisibility(accessToken, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Scene 정보 조회", description = "암호화된 SceneId를 통해 Scene 정보 조회")
    @GetMapping("/{encryptedSceneId}")
    public ResponseEntity<SceneResponse> getScene(
            @PathVariable String encryptedSceneId) {
        Long sceneId = aesUtil.decryptSceneId(encryptedSceneId);
        SceneResponse response = sceneService.getScene(sceneId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "AccessToken을 받아 암호화된 Scene ID 반환", description = "헤더에서 accessToken을 받아 사용자가 조회할 수 있는 Scene ID를 암호화하여 반환")
    @GetMapping
    public ResponseEntity<String> getEncryptedSceneId(@RequestHeader("access-token") String accessToken) {
        String socialId = kakaoAuthService.getUserInfo(accessToken).getKakao_account().getEmail();

        Long sceneId = sceneService.getSceneIdBySocialId(socialId);

        String encryptedSceneId = aesUtil.encrypt(String.valueOf(sceneId));

        return ResponseEntity.ok(encryptedSceneId);
    }

}
