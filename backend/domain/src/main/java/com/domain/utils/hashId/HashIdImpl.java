package com.domain.utils.hashId;

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
    public Long decode(String id) throws ApiHashIdException {
        if (id == null) {
            throw new ApiHashIdException("Cannot decode null string.");
        }

        final var result = hashids.decode(id);
        if (result.length != 1) {
            throw new ApiHashIdException("Cannot decode id: " + id);
        }

        return hashids.decode(id)[0];
    }
}
