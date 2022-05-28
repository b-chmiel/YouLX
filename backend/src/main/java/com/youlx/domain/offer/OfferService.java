package com.youlx.domain.offer;

import com.youlx.domain.utils.exception.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OfferService {
    Optional<Offer> findById(String id);

    Offer create(Offer offer) throws Exception;

    Offer close(String id, OfferClose offerClose, String username);

    boolean isClosable(String username, Offer offer);

    boolean isPublishable(String username, String offerId);

    Page<Offer> findBy(Pageable pageable, String username, String status);

    Page<Offer> findOpen(Pageable pageable);

    void modify(String id, OfferModify offer, String username) throws ApiException;

    boolean isOwnerOf(String offerId, String username);

    void publish(String username, String offerId) throws ApiException;

    boolean isVisible(String username, String offerId);

    List<Offer> search(String username, String query);
}
