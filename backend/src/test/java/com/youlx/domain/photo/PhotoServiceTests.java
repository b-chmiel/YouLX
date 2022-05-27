package com.youlx.domain.photo;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferService;
import com.youlx.domain.utils.ApiNotFoundException;
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
            when(offerService.findById(offerId)).thenReturn(Optional.empty());

            assertThrows(ApiNotFoundException.class, () -> service.save(offerId, photo));
        }

        @Test
        void photoInvalid() {
            final var offerId = "a";
            final var photo = new Photo("", null);
            when(offerService.findById(offerId)).thenReturn(Optional.of(new Offer(null, null, null)));

            assertThrows(ApiImageException.class, () -> service.save(offerId, photo));
        }

        @Test
        void photoValid() {
            final var offerId = "a";
            when(offerService.findById(offerId)).thenReturn(Optional.of(new Offer(null, null, null)));

            service.save(offerId, Fixtures.photo);

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

            when(offerService.findById(offerId)).thenReturn(Optional.of(new Offer("", "", null)));
            when(photoRepository.findById(photoId)).thenReturn(Optional.of(Fixtures.photo));

            service.delete(offerId, photoId);

            verify(photoRepository, times(1)).delete(offerId, photoId);
        }
    }
}
