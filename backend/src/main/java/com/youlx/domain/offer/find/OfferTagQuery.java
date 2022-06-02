package com.youlx.domain.offer.find;

import com.youlx.domain.tag.Tag;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class OfferTagQuery {
    private final List<Tag> tags;

    public OfferTagQuery(String tag) {
        this.tags = Arrays.stream(tag.split(";"))
                .filter(t -> !t.isBlank())
                .map(Tag::new)
                .toList();
    }
}
