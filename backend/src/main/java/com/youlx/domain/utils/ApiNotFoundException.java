package com.youlx.domain.utils;

import com.youlx.domain.utils.ApiException;

public class ApiNotFoundException extends ApiException {
    public ApiNotFoundException(String message) {
        super(message);
    }
}
