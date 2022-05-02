package com.youlx.infrastructure.offer;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface OfferPagedRepository extends PagingAndSortingRepository<OfferTuple, Long> {
}
