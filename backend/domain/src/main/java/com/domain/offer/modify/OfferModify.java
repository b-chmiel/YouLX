package com.domain.offer.modify;

import com.domain.tag.Tag;

import java.math.BigDecimal;
import java.util.Set;

public record OfferModify(String name, String description, BigDecimal price, Set<Tag> tags) {
}
