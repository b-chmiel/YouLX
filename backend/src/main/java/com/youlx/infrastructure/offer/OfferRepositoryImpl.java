package com.youlx.infrastructure.offer;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class OfferRepositoryImpl implements OfferRepository {
    public interface Repo extends JpaRepository<OfferTuple, Long> {
    }

    private final Repo repo;

    @Override
    public Offer create(Offer offer) {
        return repo.save(new OfferTuple(offer)).toDomain();
    }

    @Override
    public Optional<Offer> findById(Long id) {
        return repo.findById(id).map(OfferTuple::toDomain);
    }
}
