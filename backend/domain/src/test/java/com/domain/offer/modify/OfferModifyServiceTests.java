package com.domain.offer.modify;

import com.domain.offer.Offer;
import com.domain.offer.OfferRepository;
import com.domain.offer.OfferStatus;
import com.domain.offer.find.OfferFindService;
import com.domain.offer.stateCheck.OfferStateCheckService;
import com.domain.user.User;
import com.domain.user.UserId;
import com.domain.utils.exception.ApiCustomException;
import com.domain.utils.exception.ApiNotFoundException;
import com.domain.utils.exception.ApiUnauthorizedException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OfferModifyServiceTests {
    private final OfferRepository offerRepository = mock(OfferRepository.class);
    private final OfferFindService offerFindService = mock(OfferFindService.class);
    private final OfferStateCheckService offerStateCheckService = mock(OfferStateCheckService.class);

    private final OfferModifyService service = new OfferModifyServiceImpl(offerRepository, offerFindService, offerStateCheckService);

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
            when(offerStateCheckService.isClosable(new UserId(user), offer)).thenReturn(true);
            when(offerRepository.close(id, offerClose)).thenReturn(Optional.of(offer));

            service.close(id, offerClose, new UserId(user));

            verify(offerRepository, times(1)).close(id, offerClose);
        }

        @Test
        void offerNotFound() {
            final var id = "b";
            final var offerClose = new OfferClose(OfferCloseReason.EXPIRED);
            final var user = new User(List.of(), "", "", "", "", "b", "");

            when(offerRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(ApiNotFoundException.class, () -> service.close(id, offerClose, new UserId(user)));
        }

        @Test
        void offerNotClosable() {
            final var id = "aa";
            final var user = new User(List.of(), "", "", "", "", "b", "");
            final var offerClose = new OfferClose(OfferCloseReason.EXPIRED);
            final var offer = new Offer(null, null, user, null);
            offer.setStatus(OfferStatus.OPEN);

            when(offerRepository.findById(id)).thenReturn(Optional.of(offer));
            when(offerStateCheckService.isClosable(new UserId(user.getUsername()), offer)).thenReturn(false);

            assertThrows(ApiCustomException.class, () -> service.close(id, offerClose, new UserId(user.getUsername() + "a")));
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

            assertThrows(ApiNotFoundException.class, () -> service.modify(offerId, offer, new UserId(username)));
        }

        @Test
        void userNotOwnerOf() {
            final var offerId = "a";
            final var offer = new OfferModify("b", "c", BigDecimal.ONE);
            final var username = "d";

            when(offerFindService.exists(offerId)).thenReturn(true);
            when(offerStateCheckService.isOwnerOf(offerId, new UserId(username))).thenReturn(false);

            assertThrows(ApiUnauthorizedException.class, () -> service.modify(offerId, offer, new UserId(username)));
        }

        @Test
        void modify() {
            final var offerId = "a";
            final var offer = new OfferModify("b", "c", BigDecimal.ONE);
            final var username = "d";

            when(offerFindService.exists(offerId)).thenReturn(true);
            when(offerStateCheckService.isOwnerOf(offerId, new UserId(username))).thenReturn(true);
            service.modify(offerId, offer, new UserId(username));

            verify(offerRepository, times(1)).modify(offerId, offer);
        }
    }
}
