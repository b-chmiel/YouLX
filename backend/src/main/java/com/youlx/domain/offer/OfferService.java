package com.youlx.domain.offer;

import java.util.List;
import java.util.Optional;

public interface OfferService {
    Optional<Offer> findById(String id);

    Offer create(Offer offer);

    Optional<Offer> close(String id, OfferClose offerClose, String user);

    Optional<List<Offer>> findByUserId(String id);
}
