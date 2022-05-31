package com.youlx.domain.offer;

import com.youlx.domain.user.UserId;
import com.youlx.domain.utils.exception.ApiException;

public interface OfferModifyService {

    Offer create(Offer offer) throws Exception;

    Offer close(String id, OfferClose offerClose, UserId user);

    void modify(String id, OfferModify offer, UserId user) throws ApiException;

    void publish(UserId user, String offerId) throws ApiException;
}
