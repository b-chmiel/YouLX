package com.youlx.domain.utils.hashId;

import lombok.RequiredArgsConstructor;
import org.hashids.Hashids;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;

@RequiredArgsConstructor
public class HashIdImpl implements HashId {
    private final Hashids hashids;

    @Override
    public String encode(Long id) {
        return hashids.encode(id);
    }

    @Override
    public Long decode(String id) throws ApiHashIdException {
        final var result = hashids.decode(id);
        if (result.length != 1) {
            throw new ApiHashIdException("Cannot decode id: " + id);
        }

        return hashids.decode(id)[0];
    }
}
