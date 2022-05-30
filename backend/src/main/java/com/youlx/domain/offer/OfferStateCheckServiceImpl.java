package com.youlx.domain.offer;

import com.youlx.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferStateCheckServiceImpl implements OfferStateCheckService {
    private final OfferRepository offerRepository;

    @Override
    public boolean isOwnerOf(String offerId, UserId user) {
        final var offer = offerRepository.findById(offerId);
        return offer.map(o -> o.getUser().getUsername().equals(user.getUsername())).orElse(false);
    }

    @Override
    public boolean isClosable(UserId user, Offer offer) {
        return offer.getUser().getUsername().equals(user.getUsername()) && offer.getStatus().equals(OfferStatus.OPEN);
    }

    @Override
    public boolean isPublishable(UserId user, String offerId) {
        return isOwnerOf(offerId, user) &&
                offerRepository
                        .findById(offerId)
                        .map(o -> o.getStatus().equals(OfferStatus.DRAFT))
                        .orElse(false);
    }

    @Override
    public boolean isVisible(UserId user, String offerId) {
        final var offer = offerRepository.findById(offerId);
        return isVisible(user, offer);
    }

    @Override
    public boolean isVisible(UserId user, Offer offer) {
        return isVisible(user, Optional.of(offer));
    }

    private boolean isVisible(UserId user, Optional<Offer> offer) {
        return offer.map(
                o -> o.getStatus().equals(OfferStatus.OPEN) || o.getUser().getUsername().equals(user.getUsername())
        ).orElse(false);
    }
}
