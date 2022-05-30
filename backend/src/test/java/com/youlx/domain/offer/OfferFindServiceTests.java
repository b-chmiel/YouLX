package com.youlx.domain.offer;

import com.youlx.domain.user.User;
import com.youlx.domain.user.UserShallow;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.offer.OfferPagedRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.youlx.testUtils.Fixtures.offer;
import static com.youlx.testUtils.Fixtures.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OfferFindServiceTests {
    private final HashId hashId = mock(HashId.class);
    private final OfferPagedRepository offerPagedRepository = mock(OfferPagedRepository.class);
    private final OfferRepository offerRepository = mock(OfferRepository.class);
    private final OfferSearchRepository offerSearchRepository = mock(OfferSearchRepository.class);
    private final OfferStateCheckService offerStateCheckService = mock(OfferStateCheckService.class);
    private final OfferFindService service = new OfferFindServiceImpl(hashId, offerPagedRepository, offerRepository, offerSearchRepository, offerStateCheckService);

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
    class SearchTests {
        @Test
        void noOffersFound() {
            final var query = "asdf";

            when(offerSearchRepository.search(query)).thenReturn(List.of());

            assertEquals(List.of(), service.search(new UserShallow(user.getUsername()), query));
        }

        @Test
        void notVisible() {
            final var query = "asdf";

            when(offerSearchRepository.search(query)).thenReturn(List.of(offer));
            when(offerStateCheckService.isVisible(user.getUsername(), offer)).thenReturn(false);

            assertEquals(List.of(), service.search(new UserShallow(user.getUsername()), query));
        }

        @Test
        void search() {
            final var query = "asdf";

            when(offerSearchRepository.search(query)).thenReturn(List.of(offer));
            when(offerStateCheckService.isVisible(user.getUsername(), offer)).thenReturn(true);

            assertEquals(List.of(offer), service.search(new UserShallow(user.getUsername()), query));

            verify(offerSearchRepository, times(1)).search(query);
        }
    }
}
