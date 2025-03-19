package com.example.demo.scene.service;

import com.example.demo.scene.domain.Scene;
import com.example.demo.scene.dto.SceneRequest;
import com.example.demo.scene.dto.SceneResponse;
import com.example.demo.scene.dto.SceneUpdateVisibilityRequest;
import com.example.demo.scene.repository.SceneMapper;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserMapper;
import com.example.demo.user.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SceneService {

    private final SceneMapper sceneMapper;
    private final UserMapper userMapper;
    private final KakaoAuthService kakaoAuthService;

    // SCENE 생성
    @Transactional
    public void createScene(String accessToken, SceneRequest request) {
        String socialId = kakaoAuthService.getUserInfo(accessToken).getKakao_account().getEmail();

        User user = userMapper.findBySocialId(socialId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Scene scene = Scene.builder()
                .user(user)
                .theme(request.getTheme())
                .isMessageVisible(false)
                .build();

        sceneMapper.saveScene(scene);
    }

    // SCENE 수정
    @Transactional
    public void updateScene(String accessToken, Long sceneId, SceneRequest request) {
        String socialId = kakaoAuthService.getUserInfo(accessToken).getKakao_account().getEmail();

        Scene scene = sceneMapper.findBySceneId(sceneId);
        if (scene == null) {
            throw new IllegalArgumentException("Scene not found");
        }

        if (!scene.getUser().getSocialId().equals(socialId)) {
            throw new IllegalArgumentException("Unauthorized: You are not the owner of this scene.");
        }

        scene.setTheme(request.getTheme());
        sceneMapper.updateScene(scene);
    }

    // SCENE 공개 설정 수정
    @Transactional
    public void updateVisibility(String accessToken, Long sceneId, SceneUpdateVisibilityRequest request) {
        String socialId = kakaoAuthService.getUserInfo(accessToken).getKakao_account().getEmail();

        Scene scene = sceneMapper.findBySceneId(sceneId);
        if (scene == null) {
            throw new IllegalArgumentException("Scene not found");
        }

        if (!scene.getUser().getSocialId().equals(socialId)) {
            throw new IllegalArgumentException("Unauthorized: You are not the owner of this scene.");
        }

        scene.setMessageVisible(request.isMessageVisible());
        sceneMapper.updateScene(scene);
    }

    // SCENE 조회
    @Transactional(readOnly = true)
    public SceneResponse getScene(String accessToken, Long sceneId) {
        String socialId = kakaoAuthService.getUserInfo(accessToken).getKakao_account().getEmail();

        Scene scene = sceneMapper.findBySceneId(sceneId);
        if (scene == null) {
            throw new IllegalArgumentException("Scene not found");
        }

        if (!scene.getUser().getSocialId().equals(socialId)) {
            throw new IllegalArgumentException("Unauthorized: This scene is private.");
        }

        return SceneResponse.builder()
                .sceneId(scene.getSceneId())
                .theme(scene.getTheme())
                .isMessageVisible(scene.isMessageVisible())
                .ownerSocialId(scene.getUser().getSocialId())
                .build();
    }
}
