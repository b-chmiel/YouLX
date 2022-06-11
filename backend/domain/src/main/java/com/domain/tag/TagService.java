package com.domain.tag;

import com.domain.user.UserId;
import com.domain.utils.exception.ApiException;

import java.util.List;

public interface TagService {
    List<Tag> getAll(String query);

    void create(Tag tag) throws ApiException;
}
