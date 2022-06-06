package com.infrastructure.tag;

import com.domain.tag.Tag;
import com.domain.tag.TagRepository;
import com.domain.tag.TagSearchRepository;
import com.domain.utils.hashId.HashIdImpl;
import com.infrastructure.JpaConfig;
import org.hashids.Hashids;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(
        classes = {TagSearchRepositoryImpl.class, JpaConfig.class, HashIdImpl.class, Hashids.class},
        loader = AnnotationConfigContextLoader.class
)
@DataJpaTest
class TagSearchRepositoryTests {
    @Autowired
    private TagSearchRepository findRepository;
    @Autowired
    private TagRepository repository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setupBefore() {
        entityManager = entityManager.getEntityManagerFactory().createEntityManager();
        repository.clear();
    }


    @Test
    void search() throws InterruptedException {
        final var query = "word";
        final var tag = new Tag("word");

        repository.create(tag);
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();


        assertEquals(List.of(tag), repository.getAll());
        assertEquals(List.of(tag), findRepository.search(query));
    }
}
