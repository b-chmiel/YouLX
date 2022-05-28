package com.youlx.infrastructure.offer;

import com.youlx.domain.offer.OfferRepository;
import com.youlx.domain.offer.OfferSearchRepository;
import com.youlx.domain.user.UserRepository;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.JpaConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static com.youlx.testUtils.Fixtures.user;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(
        classes = {OfferSearchRepositoryImpl.class, JpaConfig.class},
        loader = AnnotationConfigContextLoader.class
)
@DataJpaTest
class OfferSearchRepositoryTests {
    @MockBean
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
        when(hashId.encode(1L)).thenReturn("1");
        when(hashId.encode(2L)).thenReturn("2");
        when(hashId.encode(3L)).thenReturn("3");
        when(hashId.encode(4L)).thenReturn("4");
        when(hashId.encode(5L)).thenReturn("5");
        when(hashId.encode(6L)).thenReturn("6");
        when(hashId.decode("1")).thenReturn(1L);
        when(hashId.decode("2")).thenReturn(2L);
        when(hashId.decode("3")).thenReturn(3L);
        when(hashId.decode("4")).thenReturn(4L);
        when(hashId.decode("5")).thenReturn(5L);
        when(hashId.decode("6")).thenReturn(6L);
        userRepository.create(user);
        offerRepository.clear();
    }

    @AfterEach
    void teardown() {
        offerRepository.clear();
    }

//    @Test
//    @Commit
//    void search() {
//        final var query = "asdf";
//        final var offer1 = new Offer("asdfffss", "asdfasdf", user, null);
//        final var offer2 = new Offer("asdfaasd ", "asdfasdf", user, null);
//        final var offer3 = new Offer("", "asdfasdf", user, null);
//        final var offer4 = new Offer("asdf", "", user, null);
//        final var offerEmpty = new Offer("", "", user, null);
//        final var offerNotFound = new Offer("aaaaaaaaa", "bbbbbbbb", user, null);
//        offerRepository.create(offer1);
//        offerRepository.create(offer2);
//        offerRepository.create(offer3);
//        offerRepository.create(offer4);
//        offerRepository.create(offerEmpty);
//        offerRepository.create(offerNotFound);
//        final var expected = List.of(offer1, offer2, offer3, offer4);
//
//        assertEquals(expected, repository.search(query));
//    }
}
