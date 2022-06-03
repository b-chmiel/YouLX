package com.domain.photo;

import com.domain.utils.exception.ApiException;

public class ApiImageException extends ApiException {
    public ApiImageException(String message) {
        super(message);
    }
}
