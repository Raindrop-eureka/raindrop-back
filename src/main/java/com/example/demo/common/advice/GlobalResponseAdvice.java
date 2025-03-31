package com.example.demo.common.advice;

import com.example.demo.common.dto.ApiResponse;
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
@RestControllerAdvice // 모든 @RestController의 응답을 가로챈다.
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

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

        // 그 외의 경우는 성공 응답으로 감싸서 반환
        return ApiResponse.success(body);
    }
}