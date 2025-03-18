package com.example.demo.scene.service;

import com.example.demo.location.domain.Location;
import com.example.demo.location.repository.LocationMapper;
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

    private final SceneMapper sceneMapper;
    private final UserMapper userMapper;
    private final LocationMapper locationMapper;
    // SCENE 생성
    @Transactional
    public Scene createScene(SceneCreateRequest request) {
        User user = userMapper.findBySocialId(request.getSocialId());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Location location = locationMapper.findByLatitudeAndLongitude(request.getLatitude(), request.getLongitude());
        if (location == null) {
            location = new Location(request.getLatitude(), request.getLongitude());
            locationMapper.saveLocation(location);
        }

        Scene scene = new Scene();
        scene.setUser(user);
        scene.setTheme(request.getTheme());
        scene.setLocation(location);
        scene.setVisible(false);

        // Scene 저장
        sceneMapper.saveScene(scene);
        return scene;
    }

    // SCENE 수정
    @Transactional
    public Scene updateScene(Long sceneId, SceneUpdateRequest request) {
        Scene scene = sceneMapper.findBySceneId(sceneId);
        scene.setTheme(request.getTheme());
        sceneMapper.updateScene(scene);
        return scene;
    }

    // SCENE 공개 설정 수정
    @Transactional
    public Scene updateVisibility(Long sceneId, SceneUpdateVisibilityRequest request) {
        Scene scene = sceneMapper.findBySceneId(sceneId);
        scene.setVisible(request.isVisible());
        sceneMapper.updateScene(scene);  // SceneMapper의 update 메서드 호출
        return scene;
    }

    // SCENE 조회
    @Transactional(readOnly = true)
    public Scene getScene(Long sceneId) {
        return sceneMapper.findBySceneId(sceneId);
    }
}
