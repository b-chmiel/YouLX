package com.youlx.domain.tag;

import com.youlx.domain.offer.OfferStateCheckService;
import com.youlx.domain.user.UserId;
import com.youlx.domain.utils.exception.ApiUnauthorizedException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class TagServiceTests {
    private final TagRepository repository = mock(TagRepository.class);
    private final OfferStateCheckService offerStateCheckService = mock(OfferStateCheckService.class);
    private final TagService service = new TagServiceImpl(repository, offerStateCheckService);

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
            when(offerStateCheckService.isOwnerOf(offerId, new UserId(username))).thenReturn(false);
            assertThrows(ApiUnauthorizedException.class, () -> service.assignToOffer(new UserId(username), offerId, tag));
        }

        @Test
        void assignToOffer() {
            final var username = "asdf";
            final var offerId = "fdsa";
            final var tag = new Tag("a");

            when(offerStateCheckService.isOwnerOf(offerId, new UserId(username))).thenReturn(true);
            service.assignToOffer(new UserId(username), offerId, tag);

            verify(repository, times(1)).assignToOffer(offerId, tag);
        }
    }
}
