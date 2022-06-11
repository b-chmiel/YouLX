package com.domain.tag;

import com.domain.utils.exception.ApiException;

import java.util.List;
import java.util.Set;

public interface TagRepository {
    List<Tag> getAll();

    void create(Tag tag) throws ApiException;

    void assignAllToOffer(String offerId, Set<Tag> tags);

    void clear();

    boolean exists(Tag tag);
}
