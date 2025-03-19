package com.example.demo.message.controller;

import com.example.demo.message.domain.Message;
import com.example.demo.message.dto.MessageDeleteRequest;
import com.example.demo.message.dto.MessageRequest;
import com.example.demo.message.dto.MessageResponse;
import com.example.demo.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Tag(name="Message",description = "메시지 정보")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    @Operation(summary = "메세지 추가", description = "특정 scene에 message를 추가")
    public ResponseEntity<Void> saveMessage(@RequestBody MessageRequest request) {
        messageService.createMessage(request);
        return ResponseEntity.ok().build();  // 성공 시 200 응답
    }

    // 특정 sceneId의 모든 메시지 조회 (쿼리 파라미터 방식)
    @GetMapping
    @Operation(summary = "메세지 조회", description = "특정 scene의 message를 조회")
    public ResponseEntity<List<MessageResponse>> getMessagesBySceneId(@RequestParam String scene) {
        List<MessageResponse> responses = messageService.getMessagesBySceneId(scene);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping
    @Operation(summary = "메세지 삭제", description = "scene 소유자가 특정 message를 삭제")
    public ResponseEntity<Void> deleteMessage(
            @RequestHeader("access-token") String accessToken,
            @RequestBody MessageDeleteRequest request) {

        messageService.deleteMessage(accessToken, request);
        return ResponseEntity.ok().build();
    }
}