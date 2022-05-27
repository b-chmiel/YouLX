package com.youlx.domain.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class ApiException extends RuntimeException {
    private final String message;
}
