package com.youlx.api.rest.offer;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
class OfferCreateDto {
    private String name;
    private String description;
    private BigDecimal price;

    Offer toDomain(User user) {
        return new Offer(name, description, user, price);
    }
}
