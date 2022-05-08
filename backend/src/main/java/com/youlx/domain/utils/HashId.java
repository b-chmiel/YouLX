package com.youlx.domain.utils;

public interface HashId {
    String encode(Long id);

    Long decode(String id) throws HashIdException;
}
