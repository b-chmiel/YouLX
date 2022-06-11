package com.infrastructure.offer;

import com.domain.offer.Offer;
import com.domain.offer.OfferRepository;
import com.domain.offer.OfferStatus;
import com.domain.offer.modify.OfferClose;
import com.domain.offer.modify.OfferModify;
import com.domain.photo.PhotoRepository;
import com.domain.tag.TagRepository;
import com.domain.utils.exception.ApiException;
import com.domain.utils.exception.ApiNotFoundException;
import com.domain.utils.hashId.ApiHashIdException;
import com.domain.utils.hashId.HashId;
import com.infrastructure.user.JpaUserRepository;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OfferRepositoryImpl implements OfferRepository {

    private final HashId hashId;
    private final JpaUserRepository userRepo;
    private final JpaOfferRepository repo;
    private final PhotoRepository photoRepository;
    private final TagRepository tagRepository;

    @Override
    public Offer create(Offer offer) throws ApiException {
        final var user = userRepo.findById(offer.getUser().getUsername());
        if (user.isEmpty()) {
            throw new ApiNotFoundException("Cannot create offer for non existing user");
        }

        final var tuple = new OfferTuple(offer, user.get());
        final var result = repo.save(tuple);

        offer.getPhotos().forEach(
                p -> photoRepository.savePhoto(hashId.encode(result.getId()), p)
        );

        tagRepository.assignAllToOffer(hashId.encode(tuple.getId()), offer.getTags());

        return result.toDomain(hashId);
    }

    @Override
    public Optional<Offer> findById(String id) {
        try {
            final Long decoded = hashId.decode(id);
            return repo.findById(decoded).map(
                    t -> t.toDomain(hashId)
            );
        } catch (ApiHashIdException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Offer> close(String id, OfferClose offer) {
        Long decoded;
        try {
            decoded = hashId.decode(id);
        } catch (ApiHashIdException e) {
            return Optional.empty();
        }

        final var found = repo.findById(decoded);
        if (found.isEmpty()) {
            return Optional.empty();
        }

        found.get().setStatus(OfferStatus.CLOSED);
        found.get().setCloseReason(offer.reason());
        found.get().setClosedDate(LocalDateTime.now());
        return Optional.of(repo.save(found.get()).toDomain(hashId));
    }

    @Override
    public void clear() {
        repo.deleteAll();
    }

    @Override
    public List<Offer> findByUserId(String id) {
        final var user = userRepo.findById(id);

        if (user.isEmpty()) {
            return List.of();
        }

        return repo
                .findAllByUser(user.get())
                .stream()
                .map(
                        o -> o.toDomain(hashId)
                ).toList();
    }

    @Override
    public void modify(String id, OfferModify offer) {
        final var tuple = repo.getById(hashId.decode(id));
        tuple.setName(offer.name());
        tuple.setDescription(offer.description());
        tuple.setPrice(offer.price());
        tuple.getTags().clear();

        tagRepository.assignAllToOffer(hashId.encode(tuple.getId()), offer.tags());

        repo.save(tuple);
    }

    @Override
    @Transactional
    public void publish(String offerId) {
        final var tuple = repo.getById(hashId.decode(offerId));
        tuple.setStatus(OfferStatus.OPEN);
        tuple.setPublishedDate(LocalDateTime.now());
        repo.save(tuple);
    }

    @Override
    public boolean exists(String offerId) {
        try {
            return repo.existsById(hashId.decode(offerId));
        } catch (ApiHashIdException e) {
            return false;
        }
    }
}
