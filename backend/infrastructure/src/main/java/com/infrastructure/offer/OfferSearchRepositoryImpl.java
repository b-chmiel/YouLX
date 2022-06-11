package com.infrastructure.offer;

import com.domain.offer.Offer;
import com.domain.offer.find.OfferSearchRepository;
import com.domain.utils.hashId.HashId;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
class OfferSearchRepositoryImpl implements OfferSearchRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private final HashId hashId;

    @Override
    public List<Offer> search(String searchString) {
        final var fullTextManager = Search.getFullTextEntityManager(entityManager);
        final var queryBuilder = fullTextManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(OfferTuple.class)
                .get();

        final var query = queryBuilder
                .keyword()
                .wildcard()
                .onFields("name", "description")
                .matching(searchString + "*")
                .createQuery();

        @SuppressWarnings("unchecked") final List<OfferTuple> result = fullTextManager.createFullTextQuery(query, OfferTuple.class).getResultList();
        return result.stream().map(p -> p.toDomain(hashId)).toList();
    }
}
