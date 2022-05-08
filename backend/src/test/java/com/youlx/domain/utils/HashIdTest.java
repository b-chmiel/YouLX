package com.youlx.domain.utils;

import org.hashids.Hashids;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class HashIdTest {
    private final HashId hashId = new HashIdImpl(new Hashids());

    @Test
    public void shouldReturnDifferentHashes() throws Exception {
        hashId.encode(1L);
        assertNotEquals(hashId.encode(0L), hashId.encode(1L));
        assertNotEquals(hashId.encode(0L), hashId.encode(2L));
    }

    @Test
    public void shouldDecode() throws Exception {
        final var id = 1234L;
        assertEquals(id, hashId.decode(hashId.encode(id)));
    }
}
