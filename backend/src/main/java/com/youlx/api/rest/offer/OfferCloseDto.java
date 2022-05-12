package com.youlx.api.rest.offer;

import com.youlx.domain.offer.OfferClose;
import com.youlx.domain.offer.OfferCloseReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class OfferCloseDto {
    private OfferCloseReason reason;

    OfferClose toDomain() {
        return new OfferClose(reason);
    }
}
