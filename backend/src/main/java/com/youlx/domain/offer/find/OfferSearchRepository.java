package com.youlx.domain.offer.find;

import com.youlx.domain.offer.Offer;

import java.util.List;

public interface OfferSearchRepository {
    List<Offer> search(String searchString);
}
