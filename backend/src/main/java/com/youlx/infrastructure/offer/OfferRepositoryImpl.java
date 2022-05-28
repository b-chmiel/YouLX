package com.youlx.infrastructure.offer;

import com.youlx.domain.offer.*;
import com.youlx.domain.photo.PhotoRepository;
import com.youlx.domain.utils.hashId.ApiHashIdException;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.user.UserTuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OfferRepositoryImpl implements OfferRepository {
    public interface Repo extends JpaRepository<OfferTuple, Long> {
        List<OfferTuple> findAllByUser(UserTuple user);
    }

    public interface UserRepo extends JpaRepository<UserTuple, Long> {
        Optional<UserTuple> getUserTupleById(String username);
    }

    private final Repo repo;
    private final HashId hashId;
    private final UserRepo userRepo;
    private final PhotoRepository photoRepository;

    @Override
    public Offer create(Offer offer) throws Exception {
        final var user = userRepo.getUserTupleById(offer.getUser().getUsername());
        if (user.isEmpty()) {
            throw new Exception("Cannot create offer for non existing user");
        }

        final var tuple = new OfferTuple(offer, user.get());
        final var result = repo.save(tuple);

        offer.getPhotos().forEach(
                p -> photoRepository.savePhoto(hashId.encode(result.getId()), p)
        );

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
        return Optional.of(repo.save(found.get()).toDomain(hashId));
    }

    @Override
    public void clear() {
        repo.deleteAll();
    }

    @Override
    public List<Offer> findByUserId(String id) {
        final var user = userRepo.getUserTupleById(id);

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
        repo.save(tuple);
    }
}
