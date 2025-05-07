package com.example.demo.scene.controller;

import com.example.demo.common.dto.ApiResponse;
import com.example.demo.scene.dto.SceneRequest;
import com.example.demo.scene.dto.SceneResponse;
import com.example.demo.scene.dto.SceneUpdateVisibilityRequest;
import com.example.demo.scene.exception.DuplicateSceneException;
import com.example.demo.scene.service.SceneService;
import com.example.demo.user.service.KakaoAuthService;
import com.example.demo.utils.AESUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scenes")
@Tag(name="Scene", description="Scene 테이블 관련 API")
public class SceneController {

    private final SceneService sceneService;
    private final AESUtil aesUtil;
    private final KakaoAuthService kakaoAuthService;

    @Operation(summary = "Scene 생성", description = "쿠키에서 액세스 토큰을 통해 Scene 생성 후 암호화된 SceneId 반환")
    @PostMapping
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "400에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "401에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "403에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "404에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<String>> createScene(
            @CookieValue(name = "access-token", required = false) String accessToken,
            @RequestBody SceneRequest request) {

        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("인증 실패"));
        }

        Long sceneId = sceneService.createScene(accessToken, request);
        String encryptedSceneId = aesUtil.encrypt(String.valueOf(sceneId));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(encryptedSceneId));
    }

    @Operation(summary = "Scene 수정 (테마 수정)", description = "쿠키에서 액세스 토큰을 통해 사용자 인증 후 Scene 테마 수정")
    @PutMapping("/theme")  // 암호화된 sceneId
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "400에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "401에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "403에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "404에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Void>> updateScene(
            @CookieValue(name = "access-token", required = false) String accessToken,
            @RequestBody SceneRequest request) {

        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("인증 실패"));
        }

        sceneService.updateScene(accessToken, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Scene 메시지 공개 상태 수정", description = "쿠키에서 액세스 토큰을 통해 사용자 인증 후 Scene의 메시지 공개 여부를 수정")
    @PutMapping("/visibility")  // 암호화된 sceneId
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "400에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "401에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "403에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "404에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Void>> updateVisibility(
            @CookieValue(name = "access-token", required = false) String accessToken,
            @RequestBody SceneUpdateVisibilityRequest request) {

        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("인증 실패"));
        }

        sceneService.updateVisibility(accessToken, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Scene 정보 조회", description = "암호화된 SceneId를 통해 Scene 정보 조회")
    @GetMapping("/{encryptedSceneId}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "400에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "401에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "403에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "404에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<SceneResponse>> getScene(
            @PathVariable String encryptedSceneId) {
        Long sceneId = aesUtil.decryptSceneId(encryptedSceneId);
        SceneResponse response = sceneService.getScene(sceneId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "AccessToken을 받아 암호화된 Scene ID 반환", description = "쿠키에서 accessToken을 받아 사용자가 조회할 수 있는 Scene ID를 암호화하여 반환")
    @GetMapping
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "400에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "401에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "403에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "404에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<String>> getEncryptedSceneId(
            @CookieValue(name = "access-token", required = false) String accessToken) {

        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("인증 실패"));
        }

        String socialId = kakaoAuthService.getUserInfo(accessToken).getKakao_account().getEmail();
        Long sceneId = sceneService.getSceneIdBySocialId(socialId);
        String encryptedSceneId = aesUtil.encrypt(String.valueOf(sceneId));

        return ResponseEntity.ok(ApiResponse.success(encryptedSceneId));
    }

    // DuplicateSceneException 처리
    @ExceptionHandler(DuplicateSceneException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateSceneException(DuplicateSceneException e) {
        ApiResponse<Void> errorResponse = ApiResponse.<Void>error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}