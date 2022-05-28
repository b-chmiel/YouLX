package com.youlx.domain.utils.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class ApiException extends RuntimeException {
    private final String message;
}
