package com.example.demo.message.service;

import com.example.demo.message.domain.Message;
import com.example.demo.message.dto.MessageRequest;
import com.example.demo.message.dto.MessageResponse;
import com.example.demo.message.repository.MessageMapper;
import com.example.demo.scene.domain.Scene;
import com.example.demo.scene.repository.SceneMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageMapper messageMapper;
    private final SceneMapper sceneMapper;

    // 특정 sceneId의 메시지 리스트 조회
    @Transactional
    public List<MessageResponse> getMessagesBySceneId(Long sceneId) {
        // Scene을 찾고, 해당 sceneId에 맞는 메시지들을 조회
        List<Message> messages = messageMapper.findMessagesBySceneId(sceneId);
        if (messages == null || messages.isEmpty()) {
            throw new IllegalArgumentException("No messages found for this scene");
        }

        // 메시지들을 MessageResponse로 변환하여 반환
        return messages.stream()
                .map(message -> new MessageResponse(
                        message.getMessageId(),
                        message.getNickname(),
                        message.getContent(),
                        message.getCreatedAt()))
                .collect(Collectors.toList());
    }

    // 메시지 생성
    @Transactional
    public Message createMessage(MessageRequest request) {
        Scene scene = sceneMapper.findBySceneId(request.getSceneId());
        if (scene == null) {
            throw new IllegalArgumentException("Scene not found");
        }

        Message message = Message.builder()
                .scene(scene)
                .nickname(request.getNickname())
                .content(request.getContent())
                .createdAt(LocalDateTime.now()) // 현재 시간 설정
                .build();

        // 메시지 저장
        messageMapper.saveMessage(message);
        return message;
    }

    // 메시지 삭제
    @Transactional
    public void deleteMessage(Long messageId) {
        messageMapper.deleteMessage(messageId);
    }
}
