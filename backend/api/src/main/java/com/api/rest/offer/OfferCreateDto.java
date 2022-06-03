package com.api.rest.offer;

import com.domain.offer.Offer;
import com.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
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
