package com.youlx.domain.offer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final OfferRepository repository;

    @Override
    public Optional<Offer> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Offer create(Offer offer) {
        return repository.create(offer);
    }

    @Override
    public Optional<Offer> close(String id, OfferClose offerClose) {
        return repository.close(id, offerClose);
    }
}
