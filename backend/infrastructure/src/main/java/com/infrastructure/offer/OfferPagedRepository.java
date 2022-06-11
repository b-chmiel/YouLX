package com.infrastructure.offer;

import com.domain.offer.OfferStatus;
import com.infrastructure.tag.TagTuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferPagedRepository extends PagingAndSortingRepository<OfferTuple, Long> {
    Page<OfferTuple> findAllByStatusIn(Pageable pageable, List<OfferStatus> status);

    Page<OfferTuple> findAllByUserIdAndStatusIn(Pageable pageable, String userId, List<OfferStatus> statuses);

    Page<OfferTuple> findAllByUserIdAndStatusInAndTagsIn(Pageable pageable, String userId, List<OfferStatus> statuses, List<TagTuple> tags);

    Page<OfferTuple> findAllByUserId(Pageable pageable, String username);

    Page<OfferTuple> findAllByStatusInAndTagsIn(Pageable pageable, List<OfferStatus> statuses, List<TagTuple> tags);
}
