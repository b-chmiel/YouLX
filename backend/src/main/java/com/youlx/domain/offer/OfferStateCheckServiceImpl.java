package com.youlx.domain.offer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferStateCheckServiceImpl implements OfferStateCheckService {
    private final OfferRepository offerRepository;

    @Override
    public boolean isOwnerOf(String offerId, String username) {
        final var offer = offerRepository.findById(offerId);
        return offer.map(o -> o.getUser().getUsername().equals(username)).orElse(false);
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
    public boolean isVisible(String username, String offerId) {
        final var offer = offerRepository.findById(offerId);
        return isVisible(username, offer);
    }

    @Override
    public boolean isVisible(String username, Offer offer) {
        return isVisible(username, Optional.of(offer));
    }

    private boolean isVisible(String username, Optional<Offer> offer) {
        return offer.map(
                o -> o.getStatus().equals(OfferStatus.OPEN) || o.getUser().getUsername().equals(username)
        ).orElse(false);
    }
}
