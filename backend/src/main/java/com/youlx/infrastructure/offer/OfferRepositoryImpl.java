package com.youlx.infrastructure.offer;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferRepository;
import com.youlx.domain.utils.HashId;
import com.youlx.domain.utils.HashIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class OfferRepositoryImpl implements OfferRepository {
    public interface Repo extends JpaRepository<OfferTuple, Long> {
    }

    private final Repo repo;
    private final HashId hashId;

    @Override
    public Offer create(Offer offer) {
        final var tuple = new OfferTuple(offer);
        final var result = repo.save(tuple);

        return result.toDomain(hashId.encode(result.getId()));
    }

    @Override
    public Optional<Offer> findById(String id) {
        try {
            final Long decoded = hashId.decode(id);
            return repo.findById(decoded).map(t -> t.toDomain(id));
        } catch (HashIdException e) {
            return Optional.empty();
        }
    }
}
