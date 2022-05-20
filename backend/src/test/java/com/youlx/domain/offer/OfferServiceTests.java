package com.youlx.domain.offer;

import com.youlx.domain.user.User;
import com.youlx.domain.utils.HashId;
import com.youlx.infrastructure.offer.OfferPagedRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
class OfferServiceTests {
    @MockBean
    private OfferRepository offerRepository;
    @MockBean
    private OfferPagedRepository offerPagedRepository;
    @MockBean
    private HashId hashId;

    private final OfferService service = new OfferServiceImpl(offerRepository, offerPagedRepository, hashId);

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
