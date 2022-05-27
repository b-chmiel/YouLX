package com.youlx.domain.utils.hashId;

public interface HashId {
    String encode(Long id);

    Long decode(String id) throws ApiHashIdException;
}
