package com.youlx.domain.offer;

import com.youlx.domain.utils.exception.ApiException;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {
    Offer create(Offer offer) throws ApiException;

    Optional<Offer> findById(String id);

    Optional<Offer> close(String id, OfferClose offer);

    void clear();

    List<Offer> findByUserId(String id);

    void modify(String id, OfferModify offer);

    void publish(String offerId);
}
