package com.youlx.domain.utils.uuid;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;


class UuidTest {
    private final Uuid uuid = new UuidImpl();

    @Test
    void generate() {
        assertNotEquals(uuid.generate(), uuid.generate());
    }
}
