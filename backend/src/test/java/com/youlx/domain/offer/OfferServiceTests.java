package com.youlx.domain.offer;

import com.youlx.domain.user.User;
import com.youlx.domain.utils.exception.ApiCustomException;
import com.youlx.domain.utils.exception.ApiNotFoundException;
import com.youlx.domain.utils.exception.ApiUnauthorizedException;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.offer.OfferPagedRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfferServiceTests {
    private final OfferRepository offerRepository = mock(OfferRepository.class);
    private final OfferPagedRepository offerPagedRepository = mock(OfferPagedRepository.class);
    private final OfferSearchRepository offerSearchRepository = mock(OfferSearchRepository.class);
    private final HashId hashId = mock(HashId.class);

    private final OfferService service = new OfferServiceImpl(offerRepository, offerPagedRepository, offerSearchRepository, hashId);

    @Nested
    class FindByIdTests {
        @Test
        void findById() {
            final var id = "a";

            service.findById(id);

            verify(offerRepository, times(1)).findById(id);
        }
    }

    @Nested
    class CreateTests {
        @Test
        void create() throws Exception {
            final var offer = new Offer(null, null, null, null);

            service.create(offer);

            verify(offerRepository, times(1)).create(offer);
        }
    }

    @Nested
    class CloseTests {
        @Test
        void close() {
            final var id = "aa";
            final var user = new User(List.of(), "", "", "", "", "b", "");
            final var offerClose = new OfferClose(OfferCloseReason.EXPIRED);
            final var offer = new Offer(null, null, user, null);
            offer.setStatus(OfferStatus.OPEN);

            when(offerRepository.findById(id)).thenReturn(Optional.of(offer));
            when(offerRepository.close(id, offerClose)).thenReturn(Optional.of(offer));

            service.close(id, offerClose, user.getUsername());

            verify(offerRepository, times(1)).close(id, offerClose);
        }

        @Test
        void offerNotFound() {
            final var id = "b";
            final var offerClose = new OfferClose(OfferCloseReason.EXPIRED);
            final var user = new User(List.of(), "", "", "", "", "b", "");

            when(offerRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(ApiNotFoundException.class, () -> service.close(id, offerClose, user.getUsername()));
        }

        @Test
        void offerNotClosable() {
            final var id = "aa";
            final var user = new User(List.of(), "", "", "", "", "b", "");
            final var offerClose = new OfferClose(OfferCloseReason.EXPIRED);
            final var offer = new Offer(null, null, user, null);
            offer.setStatus(OfferStatus.OPEN);

            when(offerRepository.findById(id)).thenReturn(Optional.of(offer));
            when(offerRepository.close(id, offerClose)).thenReturn(Optional.of(offer));

            assertThrows(ApiCustomException.class, () -> service.close(id, offerClose, user.getUsername() + "a"));
        }
    }

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

            verify(offerRepository, times(1)).findById(id);
        }
    }

    @Nested
    class ModifyTests {
        @Test
        void offerNotFound() {
            final var offerId = "a";
            final var offer = new OfferModify("b", "c", BigDecimal.ONE);
            final var username = "d";

            assertThrows(ApiNotFoundException.class, () -> service.modify(offerId, offer, username));
        }

        @Test
        void userNotOwnerOf() {
            final var offerId = "a";
            final var offer = new OfferModify("b", "c", BigDecimal.ONE);
            final var username = "d";
            final var user = new User(List.of(), "", "", "", "", username + "a", "");
            when(offerRepository.findById(offerId)).thenReturn(Optional.of(new Offer("", "", user, null)));
            assertThrows(ApiUnauthorizedException.class, () -> service.modify(offerId, offer, username));
        }

        @Test
        void modify() {
            final var offerId = "a";
            final var offer = new OfferModify("b", "c", BigDecimal.ONE);
            final var username = "d";
            final var user = new User(List.of(), "", "", "", "", username, "");
            when(offerRepository.findById(offerId)).thenReturn(Optional.of(new Offer("", "", user, null)));
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
            final var user = new User(List.of(), "", "", "", "", username + "a", "");

            when(offerRepository.findById(offerId)).thenReturn(Optional.of(new Offer("", "", user, null)));
            assertFalse(service.isOwnerOf(offerId, username));
        }

        @Test
        void isOwnerOf() {
            final var offerId = "a";
            final var username = "b";
            final var user = new User(List.of(), "", "", "", "", username, "");

            when(offerRepository.findById(offerId)).thenReturn(Optional.of(new Offer("", "", user, null)));
            assertTrue(service.isOwnerOf(offerId, username));
        }
    }

    @Nested
    class SearchTests {
        @Test
        void search() {
            final var user1 = new User(List.of(), "", "", "", "", "a", "");
            final var user2 = new User(List.of(), "", "", "", "", "b", "");
            final var query = "asdf";

            final var offer1 = new Offer("1", "", user1, null);
            offer1.setStatus(OfferStatus.OPEN);
            final var offer2 = new Offer("2", "", user2, null);
            offer2.setStatus(OfferStatus.OPEN);
            final var offer3 = new Offer("3", "", user2, null);
            final var offer4 = new Offer("4", "", user1, null);
            final var offer5 = new Offer("5", "", user1, null);
            offer5.close(OfferCloseReason.EXPIRED);
            final var offer6 = new Offer("6", "", user2, null);
            offer6.close(OfferCloseReason.EXPIRED);

            final var offers = List.of(
                    offer1, offer2, offer3, offer4, offer5, offer6
            );

            when(offerSearchRepository.search(query)).thenReturn(offers);

            final var expected = List.of(
                    offer1, offer2, offer4, offer5
            );

            assertEquals(expected, service.search(user1.getUsername(), query));

            verify(offerSearchRepository, times(1)).search(query);
        }
    }
}
