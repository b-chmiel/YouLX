package com.youlx.infrastructure.offer;

import com.youlx.domain.offer.*;
import com.youlx.domain.utils.HashId;
import com.youlx.domain.utils.HashIdException;
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
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
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

    @BeforeEach
    void setup() throws HashIdException {
        when(hashId.encode(1L)).thenReturn("1");
        when(hashId.encode(2L)).thenReturn("2");
        when(hashId.encode(3L)).thenReturn("3");
        when(hashId.decode("1")).thenReturn(1L);
        when(hashId.decode("2")).thenReturn(2L);
        when(hashId.decode("3")).thenReturn(3L);
        repository.clear();
    }

    @AfterEach
    void cleanup() {
        repository.clear();
    }

    @Test
    void shouldCreateAndFind() {
        final var offer = new Offer("3", "a", "b", OfferStatus.OPEN,  "c", LocalDateTime.now(), Optional.empty());

        final var result = repository.create(offer);

        Helpers.assertOfferAttributesEqual(offer, result);
        assertThat(repository.findById("3").get(), samePropertyValuesAs(result));
    }

    @Test
    void shouldPatch() {
        final var offer = new Offer("a", "b", "c");

        final var result = repository.create(offer);

        result.close(OfferCloseReason.EXPIRED);
        repository.close(result.getId(), new OfferClose(OfferCloseReason.EXPIRED));
        final var changed = repository.findById(result.getId()).orElse(null);

        Helpers.assertOfferAttributesEqual(result, changed);
    }

    @Test
    void shouldClose() {
        final var offer = new Offer("a", "b", "c");
        final var created = repository.create(offer);

        assertEquals(OfferStatus.OPEN, created.getStatus());

        repository.close("1", new OfferClose(OfferCloseReason.EXPIRED));

        final var result = repository.findById("1").get();
        assertEquals(OfferStatus.CLOSED, result.getStatus());
        assertEquals(OfferCloseReason.EXPIRED, result.getCloseReason().get());
    }

    @Test
    void shouldGetAllByUserId() {
        assertEquals(0, repository.findByUserId("c").size());

        repository.create(new Offer("", "", "c"));
        repository.create(new Offer("", "", "d"));

        assertEquals(1, repository.findByUserId("c").size());
    }

    private static class Helpers {
        static void assertOfferAttributesEqual(Offer expected, Offer actual) {
            assertEquals(expected.getCreationDate(), actual.getCreationDate());
            assertEquals(expected.getCloseReason(), actual.getCloseReason());
            assertEquals(expected.getDescription(), actual.getDescription());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getStatus(), actual.getStatus());
            assertEquals(expected.getUserId(), actual.getUserId());
        }
    }
}
