package com.youlx.domain.offer;

import com.youlx.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

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
    public Optional<List<Offer>> findByUserId(String id) {
        if (userRepository.findByUsername(id).isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(offerRepository.findByUserId(id));
    }

    @Override
    public boolean isClosable(UserDetails user, Offer offer) {
        return offer.getUser().getUsername().equals(user.getUsername()) && offer.getStatus().equals(OfferStatus.OPEN);
    }
}
