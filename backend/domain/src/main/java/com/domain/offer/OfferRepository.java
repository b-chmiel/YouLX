package com.domain.offer;

import com.domain.offer.modify.OfferClose;
import com.domain.offer.modify.OfferModify;
import com.domain.tag.Tag;
import com.domain.user.UserId;
import com.domain.utils.exception.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {
    Offer create(Offer offer) throws ApiException;

    Optional<Offer> findById(String id);

    Optional<Offer> close(String id, OfferClose offer);

    void clear();

    List<Offer> findByUserId(String id);

    void modify(String id, OfferModify offer);

    void publish(String offerId);

    boolean exists(String offerId);

    Page<Offer> findAllByStatusIn(Pageable pageable, List<OfferStatus> status);

    Page<Offer> findAllByUserIdAndStatusIn(Pageable pageable, UserId user, List<OfferStatus> statuses);

    Page<Offer> findAllByUserIdAndStatusInAndTagsIn(Pageable pageable, UserId user, List<OfferStatus> statuses, List<Tag> tags);

    Page<Offer> findAllByUserId(Pageable pageable, UserId user);

    Page<Offer> findAllByStatusInAndTagsIn(Pageable pageable, List<OfferStatus> statuses, List<Tag> tags);
}
