package com.youlx.domain.offer.find;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferStatus;
import com.youlx.domain.tag.Tag;
import com.youlx.domain.user.UserId;
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
