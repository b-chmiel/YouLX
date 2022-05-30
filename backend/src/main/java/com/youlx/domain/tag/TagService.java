package com.youlx.domain.tag;

import com.youlx.domain.user.UserId;
import com.youlx.domain.utils.exception.ApiException;

import java.util.List;

public interface TagService {
    List<Tag> getAll();

    void create(Tag tag) throws ApiException;

    void assignToOffer(UserId user, String offerId, Tag tag) throws ApiException;
}
