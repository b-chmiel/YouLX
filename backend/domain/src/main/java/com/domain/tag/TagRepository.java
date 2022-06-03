package com.domain.tag;

import com.domain.utils.exception.ApiException;

import java.util.List;

public interface TagRepository {
    List<Tag> getAll();

    void create(Tag tag) throws ApiException;

    void assignToOffer(String offerId, Tag tag) throws ApiException;

    void clear();
}
