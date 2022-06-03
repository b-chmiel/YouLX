package com.domain.offer.find;

import com.domain.tag.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OfferTagQueryTests {
    @Test
    void create() {
        final var query = new OfferTagQuery("one;two;three");
        assertEquals(List.of(new Tag("one"), new Tag("two"), new Tag("three")), query.getTags());
    }
}
