package com.youlx.domain.offer;

import com.youlx.domain.user.User;
import com.youlx.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class OfferServiceTests {
    @MockBean
    private OfferRepository offerRepository;

    @MockBean
    private UserRepository userRepository;

    private final OfferService service = new OfferServiceImpl(offerRepository, userRepository);

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
