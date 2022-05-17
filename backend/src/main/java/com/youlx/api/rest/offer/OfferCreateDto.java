package com.youlx.api.rest.offer;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
class OfferCreateDto {
    private String name;
    private String description;

    Offer toDomain(User user) {
        return new Offer(name, description, user);
    }
}
