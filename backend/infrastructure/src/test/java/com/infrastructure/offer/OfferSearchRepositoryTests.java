package com.infrastructure.offer;

import com.domain.offer.Offer;
import com.domain.offer.OfferRepository;
import com.domain.offer.find.OfferSearchRepository;
import com.domain.user.UserRepository;
import com.domain.utils.hashId.HashId;
import com.domain.utils.hashId.HashIdImpl;
import com.infrastructure.Fixtures;
import com.infrastructure.photo.PhotoRepositoryImpl;
import com.infrastructure.tag.TagRepositoryImpl;
import com.infrastructure.user.UserRepositoryImpl;
import org.hashids.Hashids;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ContextConfiguration(
        classes = {OfferSearchRepositoryImpl.class, HashIdImpl.class, Hashids.class, OfferRepositoryImpl.class, UserRepositoryImpl.class, PhotoRepositoryImpl.class, TagRepositoryImpl.class},
        loader = AnnotationConfigContextLoader.class
)
@EnableJpaRepositories("com.infrastructure")
@EntityScan("com.infrastructure")
@Transactional
class OfferSearchRepositoryTests {
    @Autowired
    private HashId hashId;

    @Autowired
    private OfferSearchRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.create(Fixtures.user);
        offerRepository.clear();
    }

    @AfterEach
    void teardown() {
        offerRepository.clear();
    }

    @Disabled
    @Test
    void search() {
        final var query = "asdf";
        final var offer1 = new Offer("asdfffss", "asdfasdf", Fixtures.user, null, Set.of());
        final var offer2 = new Offer("asdfaasd ", "asdfasdf", Fixtures.user, null, Set.of());
        final var offer3 = new Offer("", "asdfasdf", Fixtures.user, null, Set.of());
        final var offer4 = new Offer("asdf", "", Fixtures.user, null, Set.of());
        final var offerEmpty = new Offer("", "", Fixtures.user, null, Set.of());
        final var offerNotFound = new Offer("aaaaaaaaa", "bbbbbbbb", Fixtures.user, null, Set.of());
        offerRepository.create(offer1);
        offerRepository.create(offer2);
        offerRepository.create(offer3);
        offerRepository.create(offer4);
        offerRepository.create(offerEmpty);
        offerRepository.create(offerNotFound);
        final var expected = List.of(offer1, offer2, offer3, offer4);

        assertEquals(expected, repository.search(query));
    }
}
