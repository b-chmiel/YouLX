package com.youlx.domain.offer;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {
    Offer create(Offer offer);

    Optional<Offer> findById(String id);

    Optional<Offer> close(String id, OfferClose offer);

    void clear();

    List<Offer> findByUserId(String id);
}
