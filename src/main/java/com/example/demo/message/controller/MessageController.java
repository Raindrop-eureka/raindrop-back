package com.example.demo.message.controller;

import com.example.demo.common.dto.ApiResponse;
import com.example.demo.message.domain.Message;
import com.example.demo.message.dto.MessageDeleteRequest;
import com.example.demo.message.dto.MessageRequest;
import com.example.demo.message.dto.MessageResponse;
import com.example.demo.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name="Message",description = "메시지 정보")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    @Operation(summary = "메세지 추가", description = "특정 scene에 message를 추가")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "401 에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "401 에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "402", description = "402 에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "403 에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "씬을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Void>> saveMessage(
            @CookieValue(name = "access-token", required = false) String accessToken,
            @RequestBody MessageRequest request) {
        if (accessToken == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("인증 실패"));
        }
        messageService.createMessage(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 특정 sceneId의 모든 메시지 조회 (쿼리 파라미터 방식)
    @GetMapping
    @Operation(summary = "메세지 조회", description = "특정 scene의 message를 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "400에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "401에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "402", description = "402에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "403에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "씬을 찾을 수 없거나 메시지가 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessagesBySceneId(@RequestParam String scene) {
        List<MessageResponse> responses = messageService.getMessagesBySceneId(scene);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @DeleteMapping
    @Operation(summary = "메세지 삭제", description = "scene 소유자가 특정 message를 삭제")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "인증 실패 또는 권한 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패 또는 권한 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "402", description = "402에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "403에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "씬을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Void>> deleteMessage(
            @CookieValue(name = "access-token", required = false) String accessToken,
            @RequestBody MessageDeleteRequest request) {

        if (accessToken == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("인증 실패"));
        }
        messageService.deleteMessage(accessToken, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}