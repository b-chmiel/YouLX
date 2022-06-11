package com.infrastructure.offer;

import com.domain.offer.Offer;
import com.domain.offer.OfferRepository;
import com.domain.offer.OfferStatus;
import com.domain.offer.modify.OfferClose;
import com.domain.offer.modify.OfferCloseReason;
import com.domain.offer.modify.OfferModify;
import com.domain.tag.Tag;
import com.domain.user.UserRepository;
import com.domain.utils.hashId.ApiHashIdException;
import com.domain.utils.hashId.HashId;
import com.domain.utils.hashId.HashIdImpl;
import com.infrastructure.Fixtures;
import com.infrastructure.JpaConfig;
import com.infrastructure.user.UserTuple;
import org.hashids.Hashids;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(
        classes = {JpaConfig.class, HashIdImpl.class, Hashids.class},
        loader = AnnotationConfigContextLoader.class
)
@DataJpaTest
class OfferRepositoryTests {
    @Autowired
    private HashId hashId;

    @Autowired
    private OfferRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JpaOfferRepository repo;


    @BeforeEach
    void setup() throws ApiHashIdException {
        userRepository.create(Fixtures.user);
        repository.clear();
    }

    @AfterEach
    void cleanup() {
        repository.clear();
    }

    @Nested
    class CreateTests {
        @Test
        void create() {
            final var offer = new Offer("a", "b", Fixtures.user, List.of(), BigDecimal.TEN, Set.of());

            final var result = repository.create(offer);

            Helpers.assertOfferAttributesEqual(offer, result);
        }
    }

    @Nested
    class CloseTests {
        @Test
        void close() {
            final var offer = new Offer("a", "b", Fixtures.user, BigDecimal.ONE, Set.of());

            final var result = repository.create(offer);

            result.close(OfferCloseReason.EXPIRED);
            repository.close(result.getId(), new OfferClose(OfferCloseReason.EXPIRED));
            final var changed = repository.findById(result.getId()).orElse(null);

            Helpers.assertOfferAttributesEqual(result, changed);
        }

        @Test
        void createAndClose() {
            final var offer = new Offer("1", "a", "b", LocalDateTime.now(), Fixtures.user, List.of(), BigDecimal.TEN, Set.of());
            offer.setCloseReason(Optional.empty());
            offer.setStatus(OfferStatus.OPEN);
            final var created = repo.save(new OfferTuple(offer, new UserTuple(Fixtures.user))).toDomain(hashId);

            assertEquals(OfferStatus.OPEN, created.getStatus());

            repository.close(created.getId(), new OfferClose(OfferCloseReason.EXPIRED));

            final var result = repository.findById(created.getId()).get();
            assertEquals(OfferStatus.CLOSED, result.getStatus());
            assertEquals(OfferCloseReason.EXPIRED, result.getCloseReason().get());
        }
    }


    @Nested
    class FindByUserIdTests {
        @Test
        void findByUserId() {
            assertEquals(0, repository.findByUserId(Fixtures.user.getUsername()).size());

            repository.create(new Offer("", "", Fixtures.user, null, Set.of()));
            repository.create(new Offer("", "", Fixtures.user, null, Set.of()));

            assertEquals(2, repository.findByUserId(Fixtures.user.getUsername()).size());
        }
    }

    @Nested
    class ModifyTests {
        @Test
        void modify() {
            final var offer = new Offer("4", "", "", LocalDateTime.now(), Fixtures.user, List.of(), BigDecimal.ONE, Set.of(new Tag("tag1")));
            offer.close(OfferCloseReason.MANUAL);
            final var created = repo.save(new OfferTuple(offer, new UserTuple(Fixtures.user))).toDomain(hashId);
            final var modify = new OfferModify("a", "b", BigDecimal.TEN, Set.of(new Tag("tag2")));

            assertTrue(repository.findById(created.getId()).isPresent());
            repository.modify(created.getId(), modify);

            final var modified = repository.findById(created.getId());
            assertEquals(modify.name(), modified.get().getName());
            assertEquals(modify.description(), modified.get().getDescription());
            assertEquals(modify.price(), modified.get().getPrice());
            assertEquals(modify.tags(), modified.get().getTags());
        }
    }

    private static class Helpers {
        static void assertOfferAttributesEqual(Offer expected, Offer actual) {
            assertEquals(expected.getCreationDate(), actual.getCreationDate());
            assertEquals(expected.getCloseReason(), actual.getCloseReason());
            assertEquals(expected.getDescription(), actual.getDescription());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getStatus(), actual.getStatus());
            assertEquals(expected.getUser().getUsername(), actual.getUser().getUsername());
            assertEquals(expected.getTags(), actual.getTags());
        }
    }
}
