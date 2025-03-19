package com.example.demo.scene.controller;

import com.example.demo.scene.domain.Scene;
import com.example.demo.scene.dto.SceneRequest;
import com.example.demo.scene.dto.SceneResponse;
import com.example.demo.scene.dto.SceneUpdateVisibilityRequest;
import com.example.demo.scene.service.SceneService;
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

    @Operation(summary = "Scene 생성", description = "액세스 토큰을 통해 사용자 인증 후 Scene을 생성")
    @PostMapping
    public ResponseEntity<Void> createScene(
            @RequestHeader("access-token") String accessToken,
            @RequestBody SceneRequest request) {
        sceneService.createScene(accessToken, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Scene 수정 (테마 수정)", description = "액세스 토큰을 통해 사용자 인증 후 Scene 테마 수정")
    @PutMapping("/{sceneId}/theme")
    public ResponseEntity<Void> updateScene(
            @RequestHeader("access-token") String accessToken,
            @PathVariable Long sceneId,
            @RequestBody SceneRequest request) {
        sceneService.updateScene(accessToken, sceneId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Scene 메시지 공개 상태 수정", description = "액세스 토큰을 통해 사용자 인증 후 Scene의 메시지 공개 여부를 수정")
    @PutMapping("/{sceneId}/visibility")
    public ResponseEntity<Void> updateVisibility(
            @RequestHeader("access-token") String accessToken,
            @PathVariable Long sceneId,
            @RequestBody SceneUpdateVisibilityRequest request) {
        sceneService.updateVisibility(accessToken, sceneId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Scene 조회", description = "액세스 토큰을 통해 사용자 인증 후 해당 Scene 정보를 조회")
    @GetMapping("/{sceneId}")
    public ResponseEntity<SceneResponse> getScene(
            @RequestHeader("access-token") String accessToken,
            @PathVariable Long sceneId) {
        SceneResponse response = sceneService.getScene(accessToken, sceneId);
        return ResponseEntity.ok(response);
    }

}
