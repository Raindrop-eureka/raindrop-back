package com.example.demo.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 공통 에러
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E001", "잘못된 입력값입니다"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "E002", "리소스를 찾을 수 없습니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "E003", "인증이 필요합니다"),

    // 메시지 관련 에러
    SCENE_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "해당 씬을 찾을 수 없습니다"),
    NO_MESSAGES_FOUND(HttpStatus.NOT_FOUND, "M002", "해당 씬에 메시지가 없습니다"),
    SCENE_OWNER_MISMATCH(HttpStatus.FORBIDDEN, "M003", "씬의 소유자가 아닙니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}