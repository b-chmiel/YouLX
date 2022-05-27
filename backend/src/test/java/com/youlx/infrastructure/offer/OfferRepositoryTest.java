package com.youlx.infrastructure.offer;

import com.youlx.domain.offer.*;
import com.youlx.domain.user.UserRepository;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.domain.utils.hashId.ApiHashIdException;
import com.youlx.infrastructure.JpaConfig;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.youlx.testUtils.Fixtures.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(
        classes = JpaConfig.class,
        loader = AnnotationConfigContextLoader.class
)
@DataJpaTest
class OfferRepositoryTest {
    @MockBean
    private HashId hashId;

    @Autowired
    private OfferRepository repository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setup() throws ApiHashIdException {
        when(hashId.encode(1L)).thenReturn("1");
        when(hashId.encode(2L)).thenReturn("2");
        when(hashId.encode(3L)).thenReturn("3");
        when(hashId.decode("1")).thenReturn(1L);
        when(hashId.decode("2")).thenReturn(2L);
        when(hashId.decode("3")).thenReturn(3L);
        userRepository.create(user);
        repository.clear();
    }

    @AfterEach
    void cleanup() {
        repository.clear();
    }

    @Test
    void shouldCreateAndFind() throws Exception {
        final var offer = new Offer("3", "a", "b", OfferStatus.OPEN, LocalDateTime.now(), Optional.empty(), user, List.of());

        final var result = repository.create(offer);

        Helpers.assertOfferAttributesEqual(offer, result);
        Helpers.assertOfferAttributesEqual(result, repository.findById("3").get());
    }

    @Test
    void shouldPatch() throws Exception {
        final var offer = new Offer("a", "b", user);

        final var result = repository.create(offer);

        result.close(OfferCloseReason.EXPIRED);
        repository.close(result.getId(), new OfferClose(OfferCloseReason.EXPIRED));
        final var changed = repository.findById(result.getId()).orElse(null);

        Helpers.assertOfferAttributesEqual(result, changed);
    }

    @Test
    void shouldClose() throws Exception {
        final var offer = new Offer("a", "b", user);
        final var created = repository.create(offer);

        assertEquals(OfferStatus.OPEN, created.getStatus());

        repository.close("1", new OfferClose(OfferCloseReason.EXPIRED));

        final var result = repository.findById("1").get();
        assertEquals(OfferStatus.CLOSED, result.getStatus());
        assertEquals(OfferCloseReason.EXPIRED, result.getCloseReason().get());
    }

    @Test
    void shouldGetAllByUserId() throws Exception {
        assertEquals(0, repository.findByUserId(user.getUsername()).size());

        repository.create(new Offer("", "", user));
        repository.create(new Offer("", "", user));

        assertEquals(2, repository.findByUserId(user.getUsername()).size());
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
