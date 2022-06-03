package com.domain.offer.find;

import com.domain.user.UserId;
import com.domain.offer.Offer;
import com.domain.offer.OfferStatus;
import com.domain.tag.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OfferFindRepository {
    Page<Offer> findAllByStatusIn(Pageable pageable, List<OfferStatus> status);

    Page<Offer> findAllByUserIdAndStatusIn(Pageable pageable, UserId user, List<OfferStatus> statuses);

    Page<Offer> findAllByUserIdAndStatusInAndTagsIn(Pageable pageable, UserId user, List<OfferStatus> statuses, List<Tag> tags);

    Page<Offer> findAllByUserId(Pageable pageable, UserId user);

    Page<Offer> findAllByStatusInAndTagsIn(Pageable pageable, List<OfferStatus> statuses, List<Tag> tags);
}
