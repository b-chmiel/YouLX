package com.youlx.domain.offer;

import com.youlx.domain.utils.HashId;
import com.youlx.infrastructure.offer.OfferPagedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
    public Optional<Offer> close(String id, OfferClose offerClose, String user) {
        final var offer = offerRepository.findById(id);
        if (offer.isPresent() && offer.get().getUser().getUsername().equals(user)) {
            return offerRepository.close(id, offerClose);
        }

        return Optional.empty();
    }

    @Override
    public boolean isClosable(UserDetails user, Offer offer) {
        return offer.getUser().getUsername().equals(user.getUsername()) && offer.getStatus().equals(OfferStatus.OPEN);
    }

    @Override
    public Page<Offer> findBy(Pageable pageable, String username, String status) {
        final var statuses = Arrays.stream(status.split(";")).map(OfferStatus::fromString).toList();
        final var offersTuple = offerPagedRepository.findAllByUserIdAndStatusIn(pageable, username, statuses);

        return offersTuple.map(
                t -> t.toDomain(hashId.encode(t.getId()))
        );
    }

    @Override
    public Page<Offer> findOpen(Pageable pageable) {
        final var offersTuple = offerPagedRepository.findAllByStatus(pageable, OfferStatus.OPEN);
        return offersTuple.map(
                t -> t.toDomain(hashId.encode(t.getId()))
        );
    }
}
