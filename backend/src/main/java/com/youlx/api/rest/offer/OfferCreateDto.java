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
    private OfferStatus status;

    Offer toDomain() {
        return new Offer(null, name, description, status);
    }
}
