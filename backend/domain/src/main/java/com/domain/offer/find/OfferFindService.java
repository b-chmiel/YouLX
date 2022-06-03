package com.domain.offer.find;

import com.domain.user.UserId;
import com.domain.offer.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OfferFindService {
    boolean exists(String id);

    Optional<Offer> findById(String id);

    Page<Offer> findBy(Pageable pageable, UserId user, OfferStatusQuery statusQuery, OfferTagQuery tagQuery);

    Page<Offer> findOpen(Pageable pageable, UserId user, OfferTagQuery tagQuery);

    List<Offer> search(UserId user, String query);
}
