package com.youlx.domain.offer;

import java.util.List;

public interface OfferSearchRepository {
    List<Offer> search(String searchString) ;
}
