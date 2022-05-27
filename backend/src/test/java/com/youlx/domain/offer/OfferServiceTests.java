package com.youlx.domain.offer;

import com.youlx.domain.photo.Photo;
import com.youlx.domain.photo.PhotoRepository;
import com.youlx.domain.user.User;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.offer.OfferPagedRepository;
import com.youlx.testUtils.Fixtures;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfferServiceTests {
    private final OfferRepository offerRepository = mock(OfferRepository.class);
    private final OfferPagedRepository offerPagedRepository = mock(OfferPagedRepository.class);
    private final PhotoRepository photoRepository = mock(PhotoRepository.class);
    private final HashId hashId = mock(HashId.class);

    private final OfferService service = new OfferServiceImpl(offerRepository, offerPagedRepository, photoRepository, hashId);

    @Nested
    class IsClosableTests {
        @Test
        void isClosable() {
            final var user = new User(null, "", "", "", "", "");
            final var offer = new Offer("", "", user);

            assertTrue(service.isClosable(user, offer));
            assertFalse(service.isClosable(new User(null, "", "", "", "", "asdf"), offer));
            offer.close(OfferCloseReason.EXPIRED);
            assertFalse(service.isClosable(user, offer));
        }
    }
}
