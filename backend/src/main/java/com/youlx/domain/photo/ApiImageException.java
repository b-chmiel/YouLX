package com.youlx.domain.photo;

import com.youlx.domain.utils.exception.ApiException;

public class ApiImageException extends ApiException {
    public ApiImageException(String message) {
        super(message);
    }
}
