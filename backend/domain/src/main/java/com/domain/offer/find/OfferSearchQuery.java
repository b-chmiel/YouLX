package com.domain.offer.find;

import lombok.Getter;

@Getter
public class OfferSearchQuery {
    private final String query;

    public OfferSearchQuery(String query) {
        this.query = query == null ? null : query.trim();
    }

    public boolean isPresent() {
        return query != null && !query.equals("");
    }
}
