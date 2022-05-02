package com.youlx.domain.offer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Offer {
    private final Long id;
    private final String name;
    private final String description;
    private final OfferStatus status;
    private final String userId;
}
