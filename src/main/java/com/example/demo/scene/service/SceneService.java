package com.example.demo.scene.service;

import com.example.demo.scene.domain.Scene;
import com.example.demo.scene.dto.SceneCreateRequest;
import com.example.demo.scene.dto.SceneUpdateRequest;
import com.example.demo.scene.dto.SceneUpdateVisibilityRequest;
import com.example.demo.scene.repository.SceneRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SceneService {

    private final SceneRepository sceneRepository;
    private final UserRepository userRepository;

    // SCENE 생성
    @Transactional
    public Scene createScene(SceneCreateRequest request) {
        User user = userRepository.findById(request.getSocialId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Scene scene = new Scene();
        scene.setUser(user);
        scene.setTheme(request.getTheme());
        scene.setLatitude(request.getLatitude());
        scene.setLongitude(request.getLongitude());
        scene.setVisible(false);

        return sceneRepository.save(scene);
    }

    // SCENE 수정
    @Transactional
    public Scene updateScene(Long sceneId, SceneUpdateRequest request) {
        Scene scene = sceneRepository.findById(sceneId)
                .orElseThrow(() -> new IllegalArgumentException("Scene not found"));

        scene.setTheme(request.getTheme());

        return sceneRepository.save(scene);
    }

    // SCENE 공개 설정 수정
    @Transactional
    public Scene updateVisibility(Long sceneId, SceneUpdateVisibilityRequest request) {
        Scene scene = sceneRepository.findById(sceneId)
                .orElseThrow(() -> new IllegalArgumentException("Scene not found"));

        scene.setVisible(request.isVisible());
        return sceneRepository.save(scene); // 수정된 Scene 반환
    }


    // SCENE 조회
    @Transactional(readOnly = true)
    public Scene getScene(Long sceneId) {
        return sceneRepository.findById(sceneId)
                .orElseThrow(() -> new IllegalArgumentException("Scene not found"));
    }
}
