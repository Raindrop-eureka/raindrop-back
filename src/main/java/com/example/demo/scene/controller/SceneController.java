package com.example.demo.scene.controller;

import com.example.demo.scene.domain.Scene;
import com.example.demo.scene.dto.SceneCreateRequest;
import com.example.demo.scene.dto.SceneUpdateRequest;
import com.example.demo.scene.dto.SceneUpdateVisibilityRequest;
import com.example.demo.scene.service.SceneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scenes")
public class SceneController {

    private final SceneService sceneService;

    // SCENE 생성
    @PostMapping
    public ResponseEntity<Scene> createScene(@RequestBody SceneCreateRequest request) {
        Scene scene = sceneService.createScene(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(scene);
    }

    // SCENE 수정 (테마 수정)
    @PutMapping("/{sceneId}/theme")
    public ResponseEntity<Scene> updateScene(@PathVariable Long sceneId, @RequestBody SceneUpdateRequest request) {
        Scene scene = sceneService.updateScene(sceneId, request);
        return ResponseEntity.ok(scene);
    }

    @PutMapping("/{sceneId}/visibility")
    public ResponseEntity<Scene> updateVisibility(
            @PathVariable Long sceneId,
            @RequestBody SceneUpdateVisibilityRequest request) {
        Scene updatedScene = sceneService.updateVisibility(sceneId, request);
        return ResponseEntity.ok(updatedScene); // 200 OK로 수정된 Scene 반환
    }

    // SCENE 조회
    @GetMapping("/{sceneId}")
    public ResponseEntity<Scene> getScene(@PathVariable Long sceneId) {
        Scene scene = sceneService.getScene(sceneId);
        return ResponseEntity.ok(scene);
    }

}
