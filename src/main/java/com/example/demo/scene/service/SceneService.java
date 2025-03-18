package com.example.demo.scene.service;

import com.example.demo.scene.domain.Scene;
import com.example.demo.scene.dto.SceneCreateRequest;
import com.example.demo.scene.dto.SceneUpdateRequest;
import com.example.demo.scene.dto.SceneUpdateVisibilityRequest;
import com.example.demo.scene.repository.SceneMapper;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SceneService {

    private final SceneMapper sceneMapper;  // SceneMapper로 변경
    private final UserMapper userMapper;  // UserMapper로 변경

    // SCENE 생성
    @Transactional
    public Scene createScene(SceneCreateRequest request) {
        // User를 SocialId로 찾기
        User user = userMapper.findBySocialId(request.getSocialId());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Scene scene = new Scene();
        scene.setUser(user);
        scene.setTheme(request.getTheme());
        scene.setLatitude(request.getLatitude());
        scene.setLongitude(request.getLongitude());
        scene.setVisible(false);

        // Scene 저장
        sceneMapper.saveScene(scene);  // SceneMapper의 insert 메서드 호출
        return scene;
    }

    // SCENE 수정
    @Transactional
    public Scene updateScene(Long sceneId, SceneUpdateRequest request) {
        // Scene을 ID로 찾기
        Scene scene = sceneMapper.findBySceneId(sceneId);
        if (scene == null) {
            throw new IllegalArgumentException("Scene not found");
        }

        scene.setTheme(request.getTheme());
        sceneMapper.updateScene(scene);  // SceneMapper의 update 메서드 호출
        return scene;
    }

    // SCENE 공개 설정 수정
    @Transactional
    public Scene updateVisibility(Long sceneId, SceneUpdateVisibilityRequest request) {
        // Scene을 ID로 찾기
        Scene scene = sceneMapper.findBySceneId(sceneId);
        if (scene == null) {
            throw new IllegalArgumentException("Scene not found");
        }

        scene.setVisible(request.isVisible());
        sceneMapper.updateScene(scene);  // SceneMapper의 update 메서드 호출
        return scene;
    }

    // SCENE 조회
    @Transactional(readOnly = true)
    public Scene getScene(Long sceneId) {
        // Scene을 ID로 찾기
        Scene scene = sceneMapper.findBySceneId(sceneId);
        if (scene == null) {
            throw new IllegalArgumentException("Scene not found");
        }
        return scene;
    }
}
