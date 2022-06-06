package com.domain.offer.find;

import com.domain.offer.Offer;
import com.domain.user.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OfferFindService {
    boolean exists(String id);

    Optional<Offer> findById(String id);

    Page<Offer> findBy(Pageable pageable, UserId user, OfferStatusQuery statusQuery, OfferTagQuery tagQuery, OfferSearchQuery searchQuery);

    Page<Offer> findOpen(Pageable pageable, UserId user, OfferTagQuery tagQuery, OfferSearchQuery searchQuery);
}
