package com.youlx.domain.offer;

import java.util.Optional;

public interface OfferRepository {
    Offer create(Offer offer);

    Optional<Offer> findById(Long id);
}