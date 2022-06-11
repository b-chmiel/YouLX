package com.infrastructure.offer;

import com.domain.offer.Offer;
import com.domain.offer.OfferRepository;
import com.domain.offer.OfferStatus;
import com.domain.offer.modify.OfferClose;
import com.domain.offer.modify.OfferModify;
import com.domain.photo.PhotoRepository;
import com.domain.tag.Tag;
import com.domain.tag.TagRepository;
import com.domain.user.UserId;
import com.domain.utils.exception.ApiException;
import com.domain.utils.exception.ApiNotFoundException;
import com.domain.utils.hashId.ApiHashIdException;
import com.domain.utils.hashId.HashId;
import com.infrastructure.tag.JpaTagRepository;
import com.infrastructure.user.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    private final OfferPagedRepository offerPagedRepository;
    private final JpaTagRepository tagRepo;


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

    @Override
    public Page<Offer> findAllByStatusIn(Pageable pageable, List<OfferStatus> status) {
        return offerPagedRepository
                .findAllByStatusIn(pageable, status)
                .map(o -> o.toDomain(hashId));
    }

    @Override
    public Page<Offer> findAllByUserIdAndStatusIn(Pageable pageable, UserId user, List<OfferStatus> statuses) {
        return offerPagedRepository
                .findAllByUserIdAndStatusIn(pageable, user.getUsername(), statuses)
                .map(o -> o.toDomain(hashId));
    }

    @Override
    public Page<Offer> findAllByUserIdAndStatusInAndTagsIn(Pageable pageable, UserId user, List<OfferStatus> statuses, List<Tag> tags) {
        final var tagTuples = tags
                .stream()
                .map(Tag::name)
                .map(tagRepo::findByName)
                .flatMap(Optional::stream)
                .toList();

        return offerPagedRepository
                .findAllByUserIdAndStatusInAndTagsIn(pageable, user.getUsername(), statuses, tagTuples)
                .map(o -> o.toDomain(hashId));
    }

    @Override
    public Page<Offer> findAllByUserId(Pageable pageable, UserId user) {
        return offerPagedRepository
                .findAllByUserId(pageable, user.getUsername())
                .map(o -> o.toDomain(hashId));
    }

    @Override
    public Page<Offer> findAllByStatusInAndTagsIn(Pageable pageable, List<OfferStatus> statuses, List<Tag> tags) {
        final var tagTuples = tags
                .stream()
                .map(Tag::name)
                .map(tagRepo::findByName)
                .flatMap(Optional::stream)
                .toList();

        return offerPagedRepository
                .findAllByStatusInAndTagsIn(pageable, statuses, tagTuples)
                .map(o -> o.toDomain(hashId));
    }
}
