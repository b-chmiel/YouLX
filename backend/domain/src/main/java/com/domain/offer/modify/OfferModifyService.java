package com.domain.offer.modify;

import com.domain.user.UserId;
import com.domain.utils.exception.ApiException;
import com.domain.offer.Offer;

public interface OfferModifyService {

    Offer create(Offer offer) throws Exception;

    Offer close(String id, OfferClose offerClose, UserId user);

    void modify(String id, OfferModify offer, UserId user) throws ApiException;

    void publish(UserId user, String offerId) throws ApiException;
}
