package com.example.demo.scene.controller;

import com.example.demo.scene.domain.Scene;
import com.example.demo.scene.dto.SceneCreateRequest;
import com.example.demo.scene.dto.SceneUpdateRequest;
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

    @Operation(summary = "Scene 생성", description = "사용자의 socialId와 theme를 받아 Scene을 생성")
    @PostMapping
    public ResponseEntity<Scene> createScene(@RequestBody SceneCreateRequest request) {
        Scene scene = sceneService.createScene(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(scene);
    }

    @Operation(summary = "Scene 수정 (테마 수정)", description = "URL에서 sceneId를 받고 수정할 테마 정보를 RequestBody에서 받아 Scene 테마 수정")
    @PutMapping("/{sceneId}/theme")
    public ResponseEntity<Scene> updateScene(@PathVariable Long sceneId, @RequestBody SceneUpdateRequest request) {
        Scene scene = sceneService.updateScene(sceneId, request);
        return ResponseEntity.ok(scene);
    }

    @Operation(summary = "Scene 메시지 공개 상태 수정", description = "URL에서 sceneId를 받고 수정할 메시지 공개 상태를 RequestBody에서 받아 Scene의 메시지 공개 여부를 수정")
    @PutMapping("/{sceneId}/visibility")
    public ResponseEntity<Scene> updateVisibility(
            @PathVariable Long sceneId,
            @RequestBody SceneUpdateVisibilityRequest request) {
        Scene updatedScene = sceneService.updateVisibility(sceneId, request);
        return ResponseEntity.ok(updatedScene);
    }

    @Operation(summary = "Scene 조회", description = "URL에서 sceneId를 받아 해당 Scene 정보를 조회")
    @GetMapping("/{sceneId}")
    public ResponseEntity<Scene> getScene(@PathVariable Long sceneId) {
        Scene scene = sceneService.getScene(sceneId);
        return ResponseEntity.ok(scene);
    }

}
