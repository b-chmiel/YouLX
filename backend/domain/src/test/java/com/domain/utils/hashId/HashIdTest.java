package com.domain.utils.hashId;

import org.hashids.Hashids;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class HashIdTest {
    private final HashId hashId = new HashIdImpl(new Hashids());

    @Test
    void shouldReturnDifferentHashes() throws Exception {
        hashId.encode(1L);
        assertNotEquals(hashId.encode(0L), hashId.encode(1L));
        assertNotEquals(hashId.encode(0L), hashId.encode(2L));
    }

    @Test
    void shouldDecode() throws Exception {
        final var id = 1234L;
        assertEquals(id, hashId.decode(hashId.encode(id)));
    }
}
