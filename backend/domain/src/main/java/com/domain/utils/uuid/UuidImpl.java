package com.domain.utils.uuid;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidImpl implements Uuid{
    @Override
    public String generate() {
        return String.valueOf(UUID.randomUUID());
    }
}
