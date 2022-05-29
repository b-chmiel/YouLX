package com.youlx.domain.tag;

import com.youlx.domain.offer.OfferService;
import com.youlx.domain.utils.exception.ApiUnauthorizedException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class TagServiceTests {
    private final TagRepository repository = mock(TagRepository.class);
    private final OfferService offerService = mock(OfferService.class);
    private final TagService service = new TagServiceImpl(repository, offerService);

    @Nested
    class GetAllTests {
        @Test
        void getAll() {
            service.getAll();
            verify(repository, times(1)).getAll();
        }
    }

    @Nested
    class CreateTests {
        @Test
        void create() {
            final var tag = new Tag("asdf");
            service.create(tag);
            verify(repository, times(1)).create(tag);
        }
    }

    @Nested
    class AssignToOfferTests {
        @Test
        void notOwnerOf() {
            final var username = "asdf";
            final var offerId = "fdsa";
            final var tag = new Tag("a");
            when(offerService.isOwnerOf(offerId, username)).thenReturn(false);
            assertThrows(ApiUnauthorizedException.class, () -> service.assignToOffer(username, offerId, tag));
        }

        @Test
        void assignToOffer() {
            final var username = "asdf";
            final var offerId = "fdsa";
            final var tag = new Tag("a");

            when(offerService.isOwnerOf(offerId, username)).thenReturn(true);
            service.assignToOffer(username, offerId, tag);

            verify(repository, times(1)).assignToOffer(offerId, tag);
        }
    }
}
