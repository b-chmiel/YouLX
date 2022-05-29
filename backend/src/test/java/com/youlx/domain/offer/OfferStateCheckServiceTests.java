package com.youlx.domain.offer;

import com.youlx.domain.user.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class OfferStateCheckServiceTests {
    private final OfferRepository repository = mock(OfferRepository.class);
    private final OfferStateCheckService service = new OfferStateCheckServiceImpl(repository);

    @Nested
    class IsClosableTests {
        @Test
        void isClosable() {
            final var user = new User(null, "", "", "", "", "", "");
            final var offer = new Offer("", "", user, null);
            offer.setStatus(OfferStatus.OPEN);

            assertTrue(service.isClosable(user.getUsername(), offer));
            assertFalse(service.isClosable(user.getUsername() + "a", offer));
            offer.close(OfferCloseReason.EXPIRED);
            assertFalse(service.isClosable(user.getUsername(), offer));
        }

        @Test
        void newlyCreatedNotClosable() {
            final var user = new User(null, "", "", "", "", "", "");
            final var offer = new Offer("", "", user, null);

            assertFalse(service.isClosable(user.getUsername(), offer));
        }

        @Test
        void closedNotClosable() {
            final var user = new User(null, "", "", "", "", "", "");
            final var offer = new Offer("", "", user, null);
            offer.setStatus(OfferStatus.CLOSED);

            assertFalse(service.isClosable(user.getUsername(), offer));
        }

        @Test
        void notClosableForOtherUser() {
            final var user = new User(null, "", "", "", "", "", "");
            final var offer = new Offer("", "", user, null);
            offer.setStatus(OfferStatus.OPEN);

            assertFalse(service.isClosable(user.getUsername() + "a", offer));
        }
    }

    @Nested
    class IsPublishableTests {
        @Test
        void isPublishable() {
            final var user = new User(null, "", "", "", "", "", "");
            final var id = "a";

            service.isPublishable(user.getUsername(), id);

            verify(repository, times(1)).findById(id);
        }
    }

    @Nested
    class IsOwnerOfTests {
        @Test
        void offerNotFound() {
            final var offerId = "a";
            final var username = "b";

            when(repository.findById(offerId)).thenReturn(Optional.empty());
            assertFalse(service.isOwnerOf(offerId, username));
        }

        @Test
        void usernameDoesNotMatch() {
            final var offerId = "a";
            final var username = "b";
            final var user = new User(List.of(), "", "", "", "", username + "a", "");

            when(repository.findById(offerId)).thenReturn(Optional.of(new Offer("", "", user, null)));
            assertFalse(service.isOwnerOf(offerId, username));
        }

        @Test
        void isOwnerOf() {
            final var offerId = "a";
            final var username = "b";
            final var user = new User(List.of(), "", "", "", "", username, "");

            when(repository.findById(offerId)).thenReturn(Optional.of(new Offer("", "", user, null)));
            assertTrue(service.isOwnerOf(offerId, username));
        }
    }

}
