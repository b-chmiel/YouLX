package com.youlx.domain.photo;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferFindService;
import com.youlx.domain.offer.OfferStateCheckService;
import com.youlx.domain.user.UserId;
import com.youlx.domain.utils.exception.ApiNotFoundException;
import com.youlx.domain.utils.exception.ApiUnauthorizedException;
import com.youlx.testUtils.Fixtures;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PhotoServiceTests {
    private final PhotoRepository photoRepository = mock(PhotoRepository.class);
    private final OfferFindService offerFindService = mock(OfferFindService.class);
    private final OfferStateCheckService offerStateCheckService = mock(OfferStateCheckService.class);
    private final PhotoService service = new PhotoServiceImpl(offerFindService, photoRepository, offerStateCheckService);

    @Nested
    class SavePhotoTests {
        @Test
        void offerDoesNotExist() {
            final var offerId = "a";
            final var photo = new Photo("", null);
            final var username = "user";
            when(offerFindService.exists(offerId)).thenReturn(false);
            when(offerStateCheckService.isOwnerOf(offerId, new UserId(username))).thenReturn(true);

            assertThrows(ApiNotFoundException.class, () -> service.save(offerId, photo, new UserId(username)));
        }

        @Test
        void photoInvalid() {
            final var offerId = "a";
            final var photo = new Photo("", null);
            final var username = "user";
            when(offerFindService.exists(offerId)).thenReturn(true);
            when(offerStateCheckService.isOwnerOf(offerId, new UserId(username))).thenReturn(true);

            assertThrows(ApiImageException.class, () -> service.save(offerId, photo, new UserId(username)));
        }

        @Test
        void userIsNotOwner() {
            final var offerId = "a";
            final var photo = new Photo("", null);
            final var username = "user";
            when(offerFindService.exists(offerId)).thenReturn(true);
            when(offerStateCheckService.isOwnerOf(offerId, new UserId(username))).thenReturn(false);

            assertThrows(ApiUnauthorizedException.class, () -> service.save(offerId, photo, new UserId(username)));
        }

        @Test
        void save() {
            final var offerId = "a";
            final var username = "user";
            when(offerFindService.exists(offerId)).thenReturn(true);
            when(offerStateCheckService.isOwnerOf(offerId, new UserId(username))).thenReturn(true);

            service.save(offerId, Fixtures.photo, new UserId(username));

            verify(photoRepository, times(1)).savePhoto(offerId, Fixtures.photo);
        }
    }

    @Nested
    class FindAllForOfferTests {
        @Test
        void findAll() {
            final var id = "a";
            final var offer = new Offer("", "", null, List.of(Fixtures.photo, Fixtures.photo), null);
            when(offerFindService.findById(id)).thenReturn(Optional.of(offer));
            assertEquals(Fixtures.photo, service.findAllForOffer(id).get(0));
            assertEquals(Fixtures.photo, service.findAllForOffer(id).get(1));
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void delete() {
            final var offerId = "a";
            final var photoId = "b";
            final var username = "c";

            when(offerFindService.exists(offerId)).thenReturn(true);
            when(photoRepository.findById(photoId)).thenReturn(Optional.of(Fixtures.photo));
            when(offerStateCheckService.isOwnerOf(offerId, new UserId(username))).thenReturn(true);
            when(photoRepository.exists(photoId)).thenReturn(true);

            service.delete(offerId, photoId, new UserId(username));

            verify(photoRepository, times(1)).delete(offerId, photoId);
        }

        @Test
        void offerNotFound() {
            final var offerId = "a";
            final var photoId = "b";
            final var username = "c";

            when(offerFindService.exists(offerId)).thenReturn(false);

            assertThrows(ApiNotFoundException.class, () -> service.delete(offerId, photoId, new UserId(username)));
        }

        @Test
        void userNotOwnerOf() {
            final var offerId = "a";
            final var photoId = "b";
            final var username = "c";

            when(offerFindService.exists(offerId)).thenReturn(true);
            when(offerStateCheckService.isOwnerOf(offerId, new UserId(username))).thenReturn(false);

            assertThrows(ApiUnauthorizedException.class, () -> service.delete(offerId, photoId, new UserId(username)));
        }

        @Test
        void photoNotFound() {
            final var offerId = "a";
            final var photoId = "b";
            final var username = "c";

            when(offerFindService.exists(offerId)).thenReturn(true);
            when(offerStateCheckService.isOwnerOf(offerId, new UserId(username))).thenReturn(true);
            when(photoRepository.exists(photoId)).thenReturn(false);

            assertThrows(ApiNotFoundException.class, () -> service.delete(offerId, photoId, new UserId(username)));
        }
    }
}
