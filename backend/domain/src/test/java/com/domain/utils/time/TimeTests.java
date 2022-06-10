package com.domain.utils.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TimeTests {
    private final Time time = new TimeImpl();

    @Test
    void shouldGiveDifferentTimes() {
        assertNotEquals(time.now(), time.now());
    }
}
