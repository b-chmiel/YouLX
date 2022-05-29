package com.youlx.domain.offer;

public interface OfferStateCheckService {
    boolean isOwnerOf(String offerId, String username);

    boolean isClosable(String username, Offer offer);

    boolean isPublishable(String username, String offerId);

    boolean isVisible(String username, String offerId);
    boolean isVisible(String username, Offer offer);
}
