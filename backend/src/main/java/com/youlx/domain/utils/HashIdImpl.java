package com.youlx.domain.utils;

import lombok.RequiredArgsConstructor;
import org.hashids.Hashids;

@RequiredArgsConstructor
public class HashIdImpl implements HashId {
    private final Hashids hashids;

    @Override
    public String encode(Long id) {
        return hashids.encode(id);
    }

    @Override
    public Long decode(String id) throws HashIdException {
        final var result = hashids.decode(id);
        if (result.length != 1) {
            throw new HashIdException("Cannot decode id.");
        }

        return hashids.decode(id)[0];
    }
}
