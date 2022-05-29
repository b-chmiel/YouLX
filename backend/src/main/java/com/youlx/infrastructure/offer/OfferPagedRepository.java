package com.youlx.infrastructure.offer;

import com.youlx.domain.offer.OfferStatus;
import com.youlx.infrastructure.tag.TagTuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OfferPagedRepository extends PagingAndSortingRepository<OfferTuple, Long> {
    Page<OfferTuple> findAllByStatusIn(Pageable pageable, List<OfferStatus> status);

    Page<OfferTuple> findAllByUserIdAndStatusIn(Pageable pageable, String userId, List<OfferStatus> statuses);

    Page<OfferTuple> findAllByUserIdAndAndStatusInAndTagsIn(Pageable pageable, String userId, List<OfferStatus> statuses, List<TagTuple> tags);

    Page<OfferTuple> findAllByUserId(Pageable pageable, String username);

    Page<OfferTuple> findAllByStatusInAndTagsIn(Pageable pageable, List<OfferStatus> statuses, List<TagTuple> tags);
}
