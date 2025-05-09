package com.example.demo.message.service;

import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.common.exception.UnauthorizedException;
import com.example.demo.message.domain.Message;
import com.example.demo.message.dto.MessageDeleteRequest;
import com.example.demo.message.dto.MessageRequest;
import com.example.demo.message.dto.MessageResponse;
import com.example.demo.message.repository.MessageMapper;
import com.example.demo.scene.domain.Scene;
import com.example.demo.scene.repository.SceneMapper;
import com.example.demo.user.service.KakaoAuthService;
import com.example.demo.utils.AESUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageMapper messageMapper;
    private final SceneMapper sceneMapper;
    private final KakaoAuthService kakaoAuthService;
    private final AESUtil aesUtil;

    // 특정 sceneId의 메시지 리스트 조회
    @Transactional
    public List<MessageResponse> getMessagesBySceneId(String encryptedSceneId) {
        // 암호화된 sceneId를 복호화
        Long sceneId = aesUtil.decryptSceneId(encryptedSceneId);  // 복호화된 sceneId 사용

        // Scene을 찾고, 해당 sceneId에 맞는 메시지들을 조회
        Scene scene = sceneMapper.findBySceneId(sceneId);
        if (scene == null) {
            throw new ResourceNotFoundException("Scene not found with ID: " + sceneId);
        }

        List<Message> messages = messageMapper.findMessagesBySceneId(sceneId);
        if (messages == null || messages.isEmpty()) {
            throw new ResourceNotFoundException("No messages found for this scene");
        }

        // 메시지들을 MessageResponse로 변환하여 반환
        return messages.stream()
                .map(message -> new MessageResponse(
                        message.getMessageId(),
                        message.getNickname(),
                        message.getContent(),
                        message.getModelId(), // 3D 모델 ID 추가
                        message.getCreatedAt()))
                .collect(Collectors.toList());
    }

    // 메시지 생성
    @Transactional
    public Message createMessage(MessageRequest request) {
        // sceneId를 복호화
        Long sceneId = aesUtil.decryptSceneId(request.getSceneId());  // 복호화된 sceneId 사용

        Scene scene = sceneMapper.findBySceneId(sceneId);
        if (scene == null) {
            throw new ResourceNotFoundException("Scene not found with ID: " + request.getSceneId());
        }

        Message message = Message.builder()
                .scene(scene)
                .nickname(request.getNickname())
                .content(request.getContent())
                .modelId(request.getModelId()) // 3D 모델 ID 설정
                .createdAt(LocalDateTime.now()) // 현재 시간 설정
                .build();

        // 메시지 저장
        messageMapper.saveMessage(message);
        return message;
    }

    // 메시지 삭제 (변경 필요 없음)
    @Transactional
    public void deleteMessage(String accessToken, MessageDeleteRequest request) {
        // 1. accessToken을 이용해 Kakao 사용자 정보 조회
        String socialId = kakaoAuthService.getUserInfo(accessToken).getKakao_account().getEmail();

        // 2. 암호화된 sceneId 복호화
        Long sceneId = aesUtil.decryptSceneId(String.valueOf(request.getSceneId()));  // 복호화된 sceneId 사용

        // 3. sceneId에 해당하는 Scene 조회
        Scene scene = sceneMapper.findBySceneId(sceneId);
        if (scene == null) {
            throw new ResourceNotFoundException("Scene not found with ID: " + request.getSceneId());
        }

        // 4. social_id 검증 (Scene의 소유자와 요청자가 같은지 확인)
        if (!scene.getUser().getSocialId().equals(socialId)) {
            throw new UnauthorizedException("You are not the owner of this scene");
        }

        // 5. 메시지 삭제
        messageMapper.deleteMessage(request.getMessageId());
    }
}