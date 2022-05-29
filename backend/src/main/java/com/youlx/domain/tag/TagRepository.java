package com.youlx.domain.tag;

import com.youlx.domain.utils.exception.ApiException;

import java.util.List;

public interface TagRepository {
    List<Tag> getAll();

    void create(Tag tag) throws ApiException;
}
