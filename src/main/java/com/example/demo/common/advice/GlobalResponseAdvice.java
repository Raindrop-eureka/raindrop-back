package com.example.demo.common.advice;

import com.example.demo.common.dto.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 모든 컨트롤러 응답을 가로채서 ApiResponse 형태로 통일해주는 전역 응답 핸들러
 */
@RestControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    public GlobalResponseAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 어떤 응답에 대해서 beforeBodyWrite를 적용할지 판단하는 메서드
     * ApiResponse 타입인 경우에는 중복 포장을 방지하기 위해 false 반환
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 반환 타입이 ApiResponse라면 이미 감싼 것이므로 적용하지 않음
        return !returnType.getParameterType().equals(ApiResponse.class);
    }

    /**
     * 실제 응답 body를 가로채서 가공하는 메서드
     * - body가 이미 ApiResponse이면 그대로 반환
     * - String 타입인 경우 특별 처리
     * - 그 외에는 ApiResponse.success(body)로 감싸서 반환
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        // 이미 ApiResponse로 감싸진 경우 그대로 반환
        if (body instanceof ApiResponse) {
            return body;
        }

        // String 타입 처리 - String을 반환하는 컨트롤러 메서드 처리를 위한 특별 케이스
        if (body instanceof String) {
            try {
                // String 타입일 경우 JSON 문자열로 직접 변환하여 반환
                return objectMapper.writeValueAsString(ApiResponse.success(body));
            } catch (JsonProcessingException e) {
                // JSON 변환 실패 시 예외 발생
                throw new RuntimeException("Failed to process String response", e);
            }
        }

        // 그 외의 경우는 성공 응답으로 감싸서 반환
        return ApiResponse.success(body);
    }
}