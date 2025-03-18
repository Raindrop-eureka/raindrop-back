package com.example.demo.message.service;

import com.example.demo.message.domain.Message;
import com.example.demo.message.dto.MessageCreateRequest;
import com.example.demo.message.repository.MessageMapper;
import com.example.demo.scene.domain.Scene;
import com.example.demo.scene.repository.SceneMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageMapper messageMapper;
    private final SceneMapper sceneMapper;

    // 메시지 생성
    @Transactional
    public Message createMessage(MessageCreateRequest request) {
        Scene scene = sceneMapper.findBySceneId(request.getSceneId());
        if (scene == null) {
            throw new IllegalArgumentException("Scene not found");
        }

        Message message = new Message();
        message.setScene(scene);
        message.setNickname(request.getNickname());
        message.setContent(request.getContent());

        // 메시지 저장
        messageMapper.saveMessage(message);
        return message;
    }

    // 메시지 수정
    @Transactional
    public Message updateMessage(Long messageId, String newContent) {
        Message message = messageMapper.findByMessageId(messageId);
        if (message == null) {
            throw new IllegalArgumentException("Message not found");
        }

        message.setContent(newContent);
        messageMapper.updateMessage(message);
        return message;
    }

    // 메시지 조회
    @Transactional(readOnly = true)
    public Message getMessage(Long messageId) {
        return messageMapper.findByMessageId(messageId);
    }

    // 메시지 삭제
    @Transactional
    public void deleteMessage(Long messageId) {
        messageMapper.deleteMessage(messageId);
    }
}
