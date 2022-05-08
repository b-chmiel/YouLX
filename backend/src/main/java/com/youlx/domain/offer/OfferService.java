package com.youlx.domain.offer;

import java.util.Optional;

public interface OfferService {
    Optional<Offer> findById(String id);

    Offer create(Offer offer);
}
