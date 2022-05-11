package com.youlx.infrastructure.offer;

import com.youlx.domain.offer.OfferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OfferPagedRepository extends PagingAndSortingRepository<OfferTuple, Long> {
    Page<OfferTuple> findAllByStatus(Pageable pageable, OfferStatus status);
}
