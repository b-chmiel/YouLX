package com.youlx.domain.photo;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferService;
import com.youlx.domain.utils.ApiNotFoundException;
import com.youlx.domain.utils.ApiUnauthorizedException;
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
    private final OfferService offerService = mock(OfferService.class);
    private final PhotoService service = new PhotoServiceImpl(offerService, photoRepository);

    @Nested
    class SavePhotoTests {
        @Test
        void offerDoesNotExist() {
            final var offerId = "a";
            final var photo = new Photo("", null);
            final var username = "user";
            when(offerService.findById(offerId)).thenReturn(Optional.empty());
            when(offerService.isOwnerOf(offerId, username)).thenReturn(true);

            assertThrows(ApiNotFoundException.class, () -> service.save(offerId, photo, username));
        }

        @Test
        void photoInvalid() {
            final var offerId = "a";
            final var photo = new Photo("", null);
            final var username = "user";
            when(offerService.findById(offerId)).thenReturn(Optional.of(new Offer(null, null, null)));
            when(offerService.isOwnerOf(offerId, username)).thenReturn(true);

            assertThrows(ApiImageException.class, () -> service.save(offerId, photo, username));
        }

        @Test
        void userIsNotOwner() {
            final var offerId = "a";
            final var photo = new Photo("", null);
            final var username = "user";
            when(offerService.findById(offerId)).thenReturn(Optional.of(new Offer(null, null, null)));
            when(offerService.isOwnerOf(offerId, username)).thenReturn(false);

            assertThrows(ApiUnauthorizedException.class, () -> service.save(offerId, photo, username));
        }

        @Test
        void save() {
            final var offerId = "a";
            final var username = "user";
            when(offerService.findById(offerId)).thenReturn(Optional.of(new Offer(null, null, null)));
            when(offerService.isOwnerOf(offerId, username)).thenReturn(true);

            service.save(offerId, Fixtures.photo, username);

            verify(photoRepository, times(1)).savePhoto(offerId, Fixtures.photo);
        }
    }

    @Nested
    class FindAllForOfferTests {
        @Test
        void findAll() {
            final var id = "a";
            final var offer = new Offer("", "", null, List.of(Fixtures.photo, Fixtures.photo));
            when(offerService.findById(id)).thenReturn(Optional.of(offer));
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

            when(offerService.findById(offerId)).thenReturn(Optional.of(new Offer("", "", null)));
            when(photoRepository.findById(photoId)).thenReturn(Optional.of(Fixtures.photo));
            when(offerService.isOwnerOf(offerId, username)).thenReturn(true);

            service.delete(offerId, photoId, username);

            verify(photoRepository, times(1)).delete(offerId, photoId);
        }

        @Test
        void offerNotFound() {
            final var offerId = "a";
            final var photoId = "b";
            final var username = "c";

            when(offerService.findById(offerId)).thenReturn(Optional.empty());

            assertThrows(ApiNotFoundException.class, () -> service.delete(offerId, photoId, username));
        }

        @Test
        void userNotOwnerOf() {
            final var offerId = "a";
            final var photoId = "b";
            final var username = "c";

            when(offerService.findById(offerId)).thenReturn(Optional.of(new Offer("", "", null)));
            when(offerService.isOwnerOf(offerId, username)).thenReturn(false);

            assertThrows(ApiUnauthorizedException.class, () -> service.delete(offerId, photoId, username));
        }

        @Test
        void photoNotFound() {
            final var offerId = "a";
            final var photoId = "b";
            final var username = "c";

            when(offerService.findById(offerId)).thenReturn(Optional.of(new Offer("", "", null)));
            when(offerService.isOwnerOf(offerId, username)).thenReturn(true);
            when(photoRepository.findById(photoId)).thenReturn(Optional.empty());

            assertThrows(ApiNotFoundException.class, () -> service.delete(offerId, photoId, username));
        }
    }
}
