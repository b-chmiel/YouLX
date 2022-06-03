package com.infrastructure.offer;

import com.domain.offer.Offer;
import com.domain.offer.OfferRepository;
import com.domain.offer.find.OfferSearchRepository;
import com.domain.user.UserRepository;
import com.domain.utils.hashId.HashId;
import com.domain.utils.hashId.HashIdImpl;
import com.infrastructure.Fixtures;
import com.infrastructure.JpaConfig;
import org.hashids.Hashids;
import org.junit.jupiter.api.AfterEach;
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
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(
        classes = {OfferSearchRepositoryImpl.class, JpaConfig.class, HashIdImpl.class, Hashids.class},
        loader = AnnotationConfigContextLoader.class
)
@DataJpaTest
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
        final var offer1 = new Offer("asdfffss", "asdfasdf", Fixtures.user, null);
        final var offer2 = new Offer("asdfaasd ", "asdfasdf", Fixtures.user, null);
        final var offer3 = new Offer("", "asdfasdf", Fixtures.user, null);
        final var offer4 = new Offer("asdf", "", Fixtures.user, null);
        final var offerEmpty = new Offer("", "", Fixtures.user, null);
        final var offerNotFound = new Offer("aaaaaaaaa", "bbbbbbbb", Fixtures.user, null);
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
