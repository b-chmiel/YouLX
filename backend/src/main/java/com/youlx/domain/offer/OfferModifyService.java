package com.youlx.domain.offer;

import com.youlx.domain.utils.exception.ApiException;

public interface OfferModifyService {

    Offer create(Offer offer) throws Exception;

    Offer close(String id, OfferClose offerClose, String username);

    void modify(String id, OfferModify offer, String username) throws ApiException;

    void publish(String username, String offerId) throws ApiException;
}
