package com.youlx.domain.offer;

import com.youlx.domain.utils.ApiException;
import com.youlx.domain.utils.ApiNotFoundException;
import com.youlx.domain.utils.ApiUnauthorizedException;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.offer.OfferPagedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final OfferPagedRepository offerPagedRepository;
    private final HashId hashId;

    @Override
    public Optional<Offer> findById(String id) {
        return offerRepository.findById(id);
    }

    @Override
    public Offer create(Offer offer) throws Exception {
        return offerRepository.create(offer);
    }

    @Override
    public Optional<Offer> close(String id, OfferClose offerClose, String username) {
        final var offer = offerRepository.findById(id);
        if (offer.isPresent() && offer.get().getUser().getUsername().equals(username)) {
            return offerRepository.close(id, offerClose);
        }

        return Optional.empty();
    }

    @Override
    public boolean isClosable(UserDetails user, Offer offer) {
        return offer.getUser().getUsername().equals(user.getUsername()) && offer.getStatus().equals(OfferStatus.OPEN);
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
        return offer.isPresent() && offer.get().getUser().getUsername().equals(username);
    }
}
