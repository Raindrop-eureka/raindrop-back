package com.example.demo.common.dto;

import com.example.demo.common.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ApiResponseDTO<T> {
    private boolean success;
    private String message;
    private String code;
    private T data;
    private LocalDateTime timestamp;

    // 성공 응답 생성 메서드
    public static <T> ApiResponseDTO<T> success(T data) {
        return ApiResponseDTO.<T>builder()
                .success(true)
                .message("Success")
                .code("S000") // 성공 코드
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 실패 응답 생성 메서드
    public static <T> ApiResponseDTO<T> error(String message) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .code("E999") // 기본 에러 코드
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ErrorCode를 사용하는 에러 응답 생성 메서드
    public static <T> ApiResponseDTO<T> error(String message, String code) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .code(code)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ErrorCode enum을 직접 사용하는 메서드
    public static <T> ApiResponseDTO<T> error(ErrorCode errorCode) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 커스텀 메시지와 ErrorCode를 함께 사용하는 메서드
    public static <T> ApiResponseDTO<T> error(String message, ErrorCode errorCode) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .code(errorCode.getCode())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
}