package com.domain.offer.find;

import com.domain.offer.Offer;

import java.util.List;

public interface OfferSearchRepository {
    List<Offer> search(String searchString);
}
