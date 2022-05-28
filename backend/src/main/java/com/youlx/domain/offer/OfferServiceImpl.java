package com.youlx.domain.offer;

import com.youlx.domain.utils.exception.ApiCustomException;
import com.youlx.domain.utils.exception.ApiException;
import com.youlx.domain.utils.exception.ApiNotFoundException;
import com.youlx.domain.utils.exception.ApiUnauthorizedException;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.offer.OfferPagedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final OfferPagedRepository offerPagedRepository;
    private final OfferSearchRepository offerSearchRepository;
    private final HashId hashId;

    @Override
    public Optional<Offer> findById(String id) {
        return offerRepository.findById(id);
    }

    @Override
    public Offer create(Offer offer) throws ApiException {
        return offerRepository.create(offer);
    }

    @Override
    public Offer close(String id, OfferClose offerClose, String username) throws ApiException {
        final var offer = offerRepository.findById(id);
        if (offer.isEmpty()) {
            throw new ApiNotFoundException("Offer not found.");
        }
        if (!isClosable(username, offer.get())) {
            throw new ApiCustomException("Offer is not closable.");
        }

        final var result = offerRepository.close(id, offerClose);

        if (result.isEmpty()) {
            throw new ApiNotFoundException("Offer not found.");
        }

        return result.get();
    }

    @Override
    public boolean isClosable(String username, Offer offer) {
        return offer.getUser().getUsername().equals(username) && offer.getStatus().equals(OfferStatus.OPEN);
    }

    @Override
    public boolean isPublishable(String username, String offerId) {
        return isOwnerOf(offerId, username) &&
                offerRepository
                        .findById(offerId)
                        .map(o -> o.getStatus().equals(OfferStatus.DRAFT))
                        .orElse(false);
    }

    @Override
    @Transactional
    public Page<Offer> findBy(Pageable pageable, String username, String status) {
        final var statuses = Arrays.stream(status.split(";")).map(OfferStatus::fromString).toList();
        final var offersTuple = offerPagedRepository.findAllByUserIdAndStatusIn(pageable, username, statuses);

        return offersTuple.map(
                t -> t.toDomain(hashId)
        );
    }

    @Override
    @Transactional
    public Page<Offer> findOpen(Pageable pageable) {
        final var offersTuple = offerPagedRepository.findAllByStatus(pageable, OfferStatus.OPEN);
        return offersTuple.map(
                t -> t.toDomain(hashId)
        );
    }

    @Override
    public void modify(String id, OfferModify offer, String username) throws ApiException {
        if (findById(id).isEmpty()) {
            throw new ApiNotFoundException("Offer not found.");
        } else if (!isOwnerOf(id, username)) {
            throw new ApiUnauthorizedException("User is not the owner of offer.");
        }

        offerRepository.modify(id, offer);
    }

    @Override
    public boolean isOwnerOf(String offerId, String username) {
        final var offer = offerRepository.findById(offerId);
        return offer.map(o -> o.getUser().getUsername().equals(username)).orElse(false);
    }

    @Override
    public void publish(String username, String offerId) throws ApiException {
        if (!isPublishable(username, offerId)) {
            throw new ApiCustomException("Offer is not publishable.");
        }

        offerRepository.publish(offerId);
    }

    @Override
    public boolean isVisible(String username, String offerId) {
        final var offer = offerRepository.findById(offerId);
        return isVisible(username, offer);
    }

    @Override
    public List<Offer> search(String username, String query) {
        return offerSearchRepository
                .search(query)
                .stream()
                .filter(offer -> isVisible(username, Optional.of(offer)))
                .toList();
    }

    private boolean isVisible(String username, Optional<Offer> offer) {
        return offer.map(
                o -> o.getStatus().equals(OfferStatus.OPEN) || o.getUser().getUsername().equals(username)
        ).orElse(false);
    }
}
