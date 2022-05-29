package com.youlx.domain.offer;

import com.youlx.domain.user.User;
import com.youlx.domain.utils.exception.ApiCustomException;
import com.youlx.domain.utils.exception.ApiNotFoundException;
import com.youlx.domain.utils.exception.ApiUnauthorizedException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OfferServiceTests {
    private final OfferRepository offerRepository = mock(OfferRepository.class);
    private final OfferFindService offerFindService = mock(OfferFindService.class);
    private final OfferStateCheckService offerStateCheckService = mock(OfferStateCheckService.class);

    private final OfferModifyService service = new OfferServiceImpl(offerRepository, offerFindService, offerStateCheckService);

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
            when(offerStateCheckService.isClosable(user.getUsername(), offer)).thenReturn(true);
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
            when(offerStateCheckService.isClosable(user.getUsername(), offer)).thenReturn(false);

            assertThrows(ApiCustomException.class, () -> service.close(id, offerClose, user.getUsername() + "a"));
        }
    }

    @Nested
    class ModifyTests {
        @Test
        void offerNotFound() {
            final var offerId = "a";
            final var offer = new OfferModify("b", "c", BigDecimal.ONE);
            final var username = "d";

            when(offerFindService.exists(offerId)).thenReturn(false);

            assertThrows(ApiNotFoundException.class, () -> service.modify(offerId, offer, username));
        }

        @Test
        void userNotOwnerOf() {
            final var offerId = "a";
            final var offer = new OfferModify("b", "c", BigDecimal.ONE);
            final var username = "d";

            when(offerFindService.exists(offerId)).thenReturn(true);
            when(offerStateCheckService.isOwnerOf(offerId, username)).thenReturn(false);

            assertThrows(ApiUnauthorizedException.class, () -> service.modify(offerId, offer, username));
        }

        @Test
        void modify() {
            final var offerId = "a";
            final var offer = new OfferModify("b", "c", BigDecimal.ONE);
            final var username = "d";

            when(offerFindService.exists(offerId)).thenReturn(true);
            when(offerStateCheckService.isOwnerOf(offerId, username)).thenReturn(true);
            service.modify(offerId, offer, username);

            verify(offerRepository, times(1)).modify(offerId, offer);
        }
    }
}
