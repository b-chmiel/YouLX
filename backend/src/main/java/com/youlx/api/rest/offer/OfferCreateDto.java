package com.youlx.api.rest.offer;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
class OfferCreateDto {
    private String name;
    private String description;

    Offer toDomain(String userId) {
        return new Offer(name, description, userId);
    }
}
