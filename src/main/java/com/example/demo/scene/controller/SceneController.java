package com.example.demo.scene.controller;

import com.example.demo.scene.domain.Scene;
import com.example.demo.scene.dto.SceneCreateRequest;
import com.example.demo.scene.dto.SceneUpdateRequest;
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

    @Operation(summary = "Scene 생성", description = "사용자의 socialId와 theme를 받아 Scene을 생성")
    @PostMapping
    public ResponseEntity<Scene> createScene(@RequestBody SceneCreateRequest request) {
        Scene scene = sceneService.createScene(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(scene);
    }

    @Operation(summary = "Scene 수정 (테마 수정)", description = "URL에서 sceneId를 받고 수정할 테마 정보를 RequestBody에서 받아 Scene 테마 수정")
    @PutMapping("/{encryptedSceneId}/theme")  // 암호화된 sceneId
    public ResponseEntity<Scene> updateScene(@PathVariable String encryptedSceneId, @RequestBody SceneUpdateRequest request) {
        Long sceneId = decryptSceneId(encryptedSceneId);  // 암호화된 sceneId 복호화
        Scene scene = sceneService.updateScene(sceneId, request);
        return ResponseEntity.ok(scene);
    }

    @Operation(summary = "Scene 메시지 공개 상태 수정", description = "URL에서 sceneId를 받고 수정할 메시지 공개 상태를 RequestBody에서 받아 Scene의 메시지 공개 여부를 수정")
    @PutMapping("/{encryptedSceneId}/visibility")  // 암호화된 sceneId
    public ResponseEntity<Scene> updateVisibility(
            @PathVariable String encryptedSceneId,
            @RequestBody SceneUpdateVisibilityRequest request) {
        Long sceneId = decryptSceneId(encryptedSceneId);  // 암호화된 sceneId 복호화
        Scene updatedScene = sceneService.updateVisibility(sceneId, request);
        return ResponseEntity.ok(updatedScene);
    }

    @Operation(summary = "Scene 정보 조회", description = "URL에서 sceneId를 받아 해당 Scene 정보를 조회")
    @GetMapping("/{encryptedSceneId}")  // 암호화된 sceneId
    public ResponseEntity<Scene> getScene(@PathVariable String encryptedSceneId) {
        Long sceneId = decryptSceneId(encryptedSceneId);  // 암호화된 sceneId 복호화
        Scene scene = sceneService.getScene(sceneId);
        return ResponseEntity.ok(scene);
    }

    // 암호화된 sceneId를 복호화하는 메서드
    private Long decryptSceneId(String encryptedSceneId) {
        try {
            String decryptedId = aesUtil.decrypt(encryptedSceneId);  // 암호화된 sceneId 복호화
            return Long.parseLong(decryptedId);
        } catch (Exception e) {
            throw new IllegalArgumentException("잘못된 접근입니다. 유효하지 않은 게시판 ID입니다.");
        }
    }

    @Operation(summary = "AccessToken을 받아 암호화된 Scene ID 반환", description = "헤더에서 accessToken을 받아 사용자가 조회할 수 있는 Scene ID를 암호화하여 반환")
    @GetMapping()
    public ResponseEntity<String> getEncryptedSceneId(@RequestHeader("access-token") String accessToken) {
        String socialId = kakaoAuthService.getUserInfo(accessToken).getKakao_account().getEmail();

        // socialId를 통해 해당 Scene 조회
        Long sceneId = sceneService.getSceneIdBySocialId(socialId);

        // sceneId 암호화
        String encryptedSceneId = aesUtil.encrypt(String.valueOf(sceneId));

        return ResponseEntity.ok(encryptedSceneId);
    }

}
