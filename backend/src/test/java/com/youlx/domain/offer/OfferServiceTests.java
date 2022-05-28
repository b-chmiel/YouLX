package com.youlx.domain.offer;

import com.youlx.domain.user.User;
import com.youlx.domain.utils.ApiNotFoundException;
import com.youlx.domain.utils.ApiUnauthorizedException;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.offer.OfferPagedRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfferServiceTests {
    private final OfferRepository offerRepository = mock(OfferRepository.class);
    private final OfferPagedRepository offerPagedRepository = mock(OfferPagedRepository.class);
    private final HashId hashId = mock(HashId.class);

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

    @Nested
    class ModifyTests {
        @Test
        void offerNotFound() {
            final var offerId = "a";
            final var offer = new OfferModify("b", "c");
            final var username = "d";

            assertThrows(ApiNotFoundException.class, () -> service.modify(offerId, offer, username));
        }

        @Test
        void userNotOwnerOf() {
            final var offerId = "a";
            final var offer = new OfferModify("b", "c");
            final var username = "d";
            when(offerRepository.findById(offerId)).thenReturn(Optional.of(new Offer("", "", new User(List.of(), "", "", "", "", username + "a"))));
            assertThrows(ApiUnauthorizedException.class, () -> service.modify(offerId, offer, username));
        }

        @Test
        void modify() {
            final var offerId = "a";
            final var offer = new OfferModify("b", "c");
            final var username = "d";
            when(offerRepository.findById(offerId)).thenReturn(Optional.of(new Offer("", "", new User(List.of(), "", "", "", "", username))));
            service.modify(offerId, offer, username);

            verify(offerRepository, times(1)).modify(offerId, offer);
        }
    }

    @Nested
    class IsOwnerOfTests {
        @Test
        void offerNotFound() {
            final var offerId = "a";
            final var username = "b";

            when(offerRepository.findById(offerId)).thenReturn(Optional.empty());
            assertFalse(service.isOwnerOf(offerId, username));
        }

        @Test
        void usernameDoesNotMatch() {
            final var offerId = "a";
            final var username = "b";

            when(offerRepository.findById(offerId)).thenReturn(Optional.of(new Offer("", "", new User(List.of(), "", "", "", "", username + "a"))));
            assertFalse(service.isOwnerOf(offerId, username));
        }

        @Test
        void isOwnerOf() {
            final var offerId = "a";
            final var username = "b";

            when(offerRepository.findById(offerId)).thenReturn(Optional.of(new Offer("", "", new User(List.of(), "", "", "", "", username))));
            assertTrue(service.isOwnerOf(offerId, username));
        }
    }
}
