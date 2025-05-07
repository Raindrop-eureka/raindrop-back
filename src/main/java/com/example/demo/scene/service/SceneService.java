package com.example.demo.scene.service;

import com.example.demo.scene.domain.Scene;
import com.example.demo.scene.dto.SceneRequest;
import com.example.demo.scene.dto.SceneResponse;
import com.example.demo.scene.dto.SceneUpdateVisibilityRequest;
import com.example.demo.scene.exception.DuplicateSceneException;
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

    /**
     * Scene을 생성하는 메소드
     * 액세스 토큰을 통해 사용자 정보를 조회하고, 해당 사용자 정보로 Scene을 생성합니다.
     * @param accessToken 카카오 액세스 토큰
     * @param request Scene 생성 요청 정보 (테마)
     * @return 생성된 Scene의 ID
     */
    @Transactional
    public Long createScene(String accessToken, SceneRequest request) {
        // 유효성 검사: 액세스 토큰 검증
        if (accessToken == null || accessToken.trim().isEmpty()) {
            log.error("Access token is null or empty");
            throw new IllegalArgumentException("Access token is required");
        }

        // 유효성 검사: 테마 검증
        if (request == null || request.getTheme() == null) {
            log.error("SceneRequest or theme is null");
            throw new IllegalArgumentException("Theme is required");
        }

        log.info("Creating scene with theme: {}", request.getTheme());
        log.debug("Access token: {}", accessToken.substring(0, Math.min(10, accessToken.length())) + "...");

        // 카카오 API로부터 사용자 정보 가져오기
        var userInfo = kakaoAuthService.getUserInfo(accessToken);
        if (userInfo == null || userInfo.getKakao_account() == null) {
            log.error("Failed to get user info from Kakao");
            throw new IllegalArgumentException("Failed to get user info from Kakao");
        }

        // 이메일 정보 확인 (socialId로 사용)
        var kakaoAccount = userInfo.getKakao_account();
        if (kakaoAccount.getEmail() == null || kakaoAccount.getEmail().trim().isEmpty()) {
            log.error("Email is null or empty from Kakao account");
            throw new IllegalArgumentException("Email is required from Kakao account");
        }

        String socialId = kakaoAccount.getEmail();
        log.info("Retrieved socialId from Kakao: {}", socialId);

        Scene existingScene = sceneMapper.findBySocialId(socialId);
        if (existingScene != null) {
            log.error("Scene already exists for user with socialId: {}", socialId);
            throw new DuplicateSceneException("이미 해당 사용자의 Scene이 존재합니다: " + socialId);
        }

        // 사용자 정보 조회
        User user = userMapper.findBySocialId(socialId);
        log.info("Found user: {}", user != null ? "yes" : "no");

        // 사용자가 존재하지 않으면 예외 발생
        if (user == null) {
            log.error("User not found with socialId: {}", socialId);
            throw new IllegalArgumentException("User not found with socialId: " + socialId);
        }

        // 사용자의 socialId 확인
        if (user.getSocialId() == null || user.getSocialId().trim().isEmpty()) {
            log.error("User has invalid socialId: {}", user.getSocialId());
            throw new IllegalArgumentException("User has invalid socialId");
        }

        log.info("User socialId: {}", user.getSocialId());

        // Scene 객체 생성
        Scene scene = Scene.builder()
                .user(user)
                .theme(request.getTheme())
                .isMessageVisible(false)
                .build();

        // 디버깅 로그
        log.debug("Built scene with theme: {}", scene.getTheme());
        log.debug("Scene's user is null? {}", scene.getUser() == null);
        log.debug("Scene's user's socialId: {}",
                scene.getUser() != null ? scene.getUser().getSocialId() : "null");

        // Scene 저장 전 최종 검증
        if (scene.getUser() == null || scene.getUser().getSocialId() == null) {
            log.error("Scene's user or user's socialId is null before saving");
            throw new IllegalStateException("Cannot save scene with null user or socialId");
        }

        // Scene 저장
        try {
            sceneMapper.saveScene(scene);
            log.info("Scene saved successfully with ID: {}", scene.getSceneId());
        } catch (Exception e) {
            log.error("Error saving scene: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save scene: " + e.getMessage(), e);
        }

        return scene.getSceneId();
    }

    /**
     * Scene의 테마를 수정하는 메소드
     * @param accessToken 카카오 액세스 토큰
     * @param request Scene 수정 요청 정보 (테마)
     */
    @Transactional
    public void updateScene(String accessToken, SceneRequest request) {
        // 유효성 검사
        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new IllegalArgumentException("Access token is required");
        }

        if (request == null || request.getTheme() == null) {
            throw new IllegalArgumentException("Theme is required");
        }

        // 카카오 API로부터 사용자 정보 가져오기
        String socialId = kakaoAuthService.getUserInfo(accessToken).getKakao_account().getEmail();
        if (socialId == null || socialId.trim().isEmpty()) {
            throw new IllegalArgumentException("Failed to get valid socialId from Kakao");
        }

        // Scene 조회
        Scene scene = sceneMapper.findBySocialId(socialId);
        if (scene == null) {
            throw new IllegalArgumentException("Scene not found for user with socialId: " + socialId);
        }

        // Scene 테마 수정
        scene.setTheme(request.getTheme());
        sceneMapper.updateScene(scene);
        log.info("Scene theme updated successfully for socialId: {}", socialId);
    }

    /**
     * Scene의 메시지 공개 설정을 수정하는 메소드
     * @param accessToken 카카오 액세스 토큰
     * @param request Scene 공개 설정 수정 요청 정보
     */
    @Transactional
    public void updateVisibility(String accessToken, SceneUpdateVisibilityRequest request) {
        // 유효성 검사
        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new IllegalArgumentException("Access token is required");
        }

        if (request == null) {
            throw new IllegalArgumentException("Visibility request is required");
        }

        // 카카오 API로부터 사용자 정보 가져오기
        String socialId = kakaoAuthService.getUserInfo(accessToken).getKakao_account().getEmail();
        if (socialId == null || socialId.trim().isEmpty()) {
            throw new IllegalArgumentException("Failed to get valid socialId from Kakao");
        }

        // Scene 조회
        Scene scene = sceneMapper.findBySocialId(socialId);
        if (scene == null) {
            throw new IllegalArgumentException("Scene not found for user with socialId: " + socialId);
        }

        // Scene 공개 설정 수정
        scene.setMessageVisible(request.isMessageVisible());
        sceneMapper.updateScene(scene);
        log.info("Scene visibility updated successfully for socialId: {}", socialId);
    }

    /**
     * Scene 정보를 조회하는 메소드
     * @param sceneId Scene ID
     * @return Scene 응답 정보
     */
    @Transactional(readOnly = true)
    public SceneResponse getScene(Long sceneId) {
        // 유효성 검사
        if (sceneId == null) {
            throw new IllegalArgumentException("Scene ID is required");
        }

        // Scene 조회
        Scene scene = sceneMapper.findBySceneId(sceneId);
        if (scene == null) {
            throw new IllegalArgumentException("Scene not found with ID: " + sceneId);
        }

        // Scene 정보로 응답 객체 생성
        return SceneResponse.builder()
                .sceneId(scene.getSceneId())
                .theme(scene.getTheme())
                .isMessageVisible(scene.isMessageVisible())
                .ownerSocialId(scene.getUser() != null ? scene.getUser().getSocialId() : null)
                .ownerNickname(scene.getUser() != null ? scene.getUser().getName() : null)
                .ownerProfileImage(scene.getUser() != null ? scene.getUser().getProfileImageUrl() : null)
                .build();
    }

    /**
     * socialId로 Scene을 찾아서 sceneId 반환하는 메소드
     * @param socialId 소셜 ID (이메일)
     * @return Scene ID
     */
    @Transactional(readOnly = true)
    public Long getSceneIdBySocialId(String socialId) {
        // 유효성 검사
        if (socialId == null || socialId.trim().isEmpty()) {
            throw new IllegalArgumentException("Social ID is required");
        }

        // Scene 조회
        Scene scene = sceneMapper.findBySocialId(socialId);
        if (scene == null) {
            throw new IllegalArgumentException("해당 socialId에 맞는 Scene을 찾을 수 없습니다: " + socialId);
        }

        log.info("Found scene with ID: {} for socialId: {}", scene.getSceneId(), socialId);
        return scene.getSceneId();
    }
}