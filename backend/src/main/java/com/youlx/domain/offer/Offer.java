package com.youlx.domain.offer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Offer {
    private final String id;
    private final String name;
    private final String description;
    private final OfferStatus status;
    private final String userId;

    public Offer(String name, String description, String userId) {
        this.id = null;
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.status = OfferStatus.OPEN;
    }
}
