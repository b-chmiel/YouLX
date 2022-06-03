package com.infrastructure.tag;

import com.domain.tag.Tag;
import com.domain.tag.TagSearchRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagSearchRepositoryImpl implements TagSearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> search(String searchString) {
        final var fullTextManager = Search.getFullTextEntityManager(entityManager);
        final var queryBuilder = fullTextManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(TagTuple.class)
                .get();

        final var query = queryBuilder
                .keyword()
                .onFields("name")
                .matching(searchString)
                .createQuery();

        @SuppressWarnings("unchecked") final List<TagTuple> result = fullTextManager.createFullTextQuery(query, TagTuple.class).getResultList();
        return result.stream().map(TagTuple::toDomain).toList();
    }
}
