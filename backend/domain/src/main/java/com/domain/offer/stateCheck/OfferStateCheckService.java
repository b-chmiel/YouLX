package com.domain.offer.stateCheck;

import com.domain.offer.Offer;
import com.domain.user.UserId;

public interface OfferStateCheckService {
    boolean isOwnerOf(String offerId, UserId user);

    boolean isClosable(UserId user, Offer offer);

    boolean isPublishable(UserId user, String offerId);

    boolean isVisible(UserId user, String offerId);
    boolean isVisible(UserId user, Offer offer);
}
