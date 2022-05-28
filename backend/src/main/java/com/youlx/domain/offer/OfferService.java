package com.youlx.domain.offer;

import com.youlx.domain.utils.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface OfferService {
    Optional<Offer> findById(String id);

    Offer create(Offer offer) throws Exception;

    Optional<Offer> close(String id, OfferClose offerClose, String username);

    boolean isClosable(UserDetails user, Offer offer);

    Page<Offer> findBy(Pageable pageable, String username, String status);

    Page<Offer> findOpen(Pageable pageable);

    void modify(String id, OfferModify offer, String username) throws ApiException;

    boolean isOwnerOf(String offerId, String username);
}
