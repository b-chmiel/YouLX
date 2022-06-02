package com.youlx.infrastructure.offer;

import com.youlx.domain.offer.*;
import com.youlx.domain.offer.modify.OfferClose;
import com.youlx.domain.offer.modify.OfferCloseReason;
import com.youlx.domain.offer.modify.OfferModify;
import com.youlx.domain.user.UserRepository;
import com.youlx.domain.utils.hashId.ApiHashIdException;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.JpaConfig;
import com.youlx.infrastructure.user.UserTuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import static com.youlx.testUtils.Fixtures.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(
        classes = JpaConfig.class,
        loader = AnnotationConfigContextLoader.class
)
@DataJpaTest
class OfferRepositoryTests {
    @MockBean
    private HashId hashId;

    @Autowired
    private OfferRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JpaOfferRepository repo;


    @BeforeEach
    void setup() throws ApiHashIdException {
        when(hashId.encode(1L)).thenReturn("1");
        when(hashId.encode(2L)).thenReturn("2");
        when(hashId.encode(3L)).thenReturn("3");
        when(hashId.encode(4L)).thenReturn("4");
        when(hashId.decode("1")).thenReturn(1L);
        when(hashId.decode("2")).thenReturn(2L);
        when(hashId.decode("3")).thenReturn(3L);
        when(hashId.decode("4")).thenReturn(4L);
        userRepository.create(user);
        repository.clear();
    }

    @AfterEach
    void cleanup() {
        repository.clear();
    }

    @Test
    void shouldCreate() {
        final var offer = new Offer("a", "b", user, List.of(), BigDecimal.TEN);

        final var result = repository.create(offer);

        Helpers.assertOfferAttributesEqual(offer, result);
    }

    @Test
    void shouldPatch() {
        final var offer = new Offer("a", "b", user, BigDecimal.ONE);

        final var result = repository.create(offer);

        result.close(OfferCloseReason.EXPIRED);
        repository.close(result.getId(), new OfferClose(OfferCloseReason.EXPIRED));
        final var changed = repository.findById(result.getId()).orElse(null);

        Helpers.assertOfferAttributesEqual(result, changed);
    }

    @Test
    void shouldClose() {
        final var offer = new Offer("1", "a", "b", LocalDateTime.now(), user, List.of(), BigDecimal.TEN, Set.of());
        offer.setCloseReason(Optional.empty());
        offer.setStatus(OfferStatus.OPEN);
        final var created = repo.save(new OfferTuple(offer, new UserTuple(user))).toDomain(hashId);

        assertEquals(OfferStatus.OPEN, created.getStatus());

        repository.close(created.getId(), new OfferClose(OfferCloseReason.EXPIRED));

        final var result = repository.findById(created.getId()).get();
        assertEquals(OfferStatus.CLOSED, result.getStatus());
        assertEquals(OfferCloseReason.EXPIRED, result.getCloseReason().get());
    }

    @Test
    void shouldGetAllByUserId() {
        assertEquals(0, repository.findByUserId(user.getUsername()).size());

        repository.create(new Offer("", "", user, null));
        repository.create(new Offer("", "", user, null));

        assertEquals(2, repository.findByUserId(user.getUsername()).size());
    }

    @Test
    void shouldModify() {
        final var offer = new Offer("4", "", "", LocalDateTime.now(), user, List.of(), BigDecimal.ONE, Set.of());
        offer.close(OfferCloseReason.MANUAL);
        final var created = repo.save(new OfferTuple(offer, new UserTuple(user))).toDomain(hashId);
        final var modify = new OfferModify("a", "b", BigDecimal.TEN);

        assertTrue(repository.findById(created.getId()).isPresent());
        repository.modify(created.getId(), modify);

        final var modified = repository.findById(created.getId());
        assertEquals(modify.name(), modified.get().getName());
        assertEquals(modify.description(), modified.get().getDescription());
        assertEquals(modify.price(), modified.get().getPrice());
    }

    private static class Helpers {
        static void assertOfferAttributesEqual(Offer expected, Offer actual) {
            assertEquals(expected.getCreationDate(), actual.getCreationDate());
            assertEquals(expected.getCloseReason(), actual.getCloseReason());
            assertEquals(expected.getDescription(), actual.getDescription());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getStatus(), actual.getStatus());
            assertEquals(expected.getUser().getUsername(), actual.getUser().getUsername());
        }
    }
}
