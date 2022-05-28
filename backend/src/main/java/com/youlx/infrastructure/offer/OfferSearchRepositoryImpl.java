package com.youlx.infrastructure.offer;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferSearchRepository;
import com.youlx.domain.utils.hashId.HashId;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequiredArgsConstructor
@Repository
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
                .onFields("name", "description")
                .matching(searchString)
                .createQuery();

        @SuppressWarnings("unchecked") final List<OfferTuple> result = fullTextManager.createFullTextQuery(query, OfferTuple.class).getResultList();
        return result.stream().map(p -> p.toDomain(hashId)).toList();
    }
}
