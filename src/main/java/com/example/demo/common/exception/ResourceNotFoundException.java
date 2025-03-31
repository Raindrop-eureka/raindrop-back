package com.example.demo.common.exception;

public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String message) {
        super(message, ErrorCode.RESOURCE_NOT_FOUND);
    }

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}