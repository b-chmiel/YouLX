package com.domain.offer.find;

import com.domain.Fixtures;
import com.domain.offer.Offer;
import com.domain.offer.OfferRepository;
import com.domain.offer.stateCheck.OfferStateCheckService;
import com.domain.user.UserId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.domain.Fixtures.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OfferFindServiceTests {
    private final OfferRepository offerRepository = mock(OfferRepository.class);
    private final OfferSearchRepository offerSearchRepository = mock(OfferSearchRepository.class);
    private final OfferStateCheckService offerStateCheckService = mock(OfferStateCheckService.class);
    private final OfferFindService service = new OfferFindServiceImpl(offerRepository, offerSearchRepository, offerStateCheckService);

    @Nested
    class ExistsTests {
        @Test
        void exists() {
            final var id = "a";
            service.exists(id);
            verify(offerRepository, times(1)).exists(id);
        }
    }

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
    class FindByTests {
        @Test
        void noOffersFound() {
            final var query = "asdf";

            when(offerSearchRepository.search(query)).thenReturn(List.of());

            assertEquals(new PageImpl<>(List.of()), service.findOpen(Pageable.unpaged(), new UserId(user), new OfferTagQuery(""), new OfferSearchQuery(query)));
            verify(offerSearchRepository, times(1)).search(query);
        }

        @Test
        void notVisible() {
            final var query = "asdf";

            when(offerSearchRepository.search(query)).thenReturn(List.of(Fixtures.offer));
            when(offerStateCheckService.isVisible(new UserId(user.getUsername()), Fixtures.offer)).thenReturn(false);

            assertEquals(new PageImpl<>(List.of()), service.findBy(Pageable.unpaged(), new UserId(user), new OfferStatusQuery(""), new OfferTagQuery(""), new OfferSearchQuery(query)));
            verify(offerSearchRepository, times(1)).search(query);
        }

        @Test
        void search() {
            final var query = "asdf";

            when(offerSearchRepository.search(query)).thenReturn(List.of(Fixtures.offer));
            when(offerStateCheckService.isVisible(new UserId(user.getUsername()), Fixtures.offer)).thenReturn(true);

            assertEquals(new PageImpl<>(List.of(Fixtures.offer)), service.findOpen(Pageable.unpaged(), new UserId(user), new OfferTagQuery(""), new OfferSearchQuery(query)));

            verify(offerSearchRepository, times(1)).search(query);
        }

        @Test
        void findAll() {
            final var page = Pageable.unpaged();
            final var user = new UserId();
            final var statusQuery = new OfferStatusQuery("OPEN");
            final var tagQuery = new OfferTagQuery("");
            final var searchQuery = new OfferSearchQuery("");

            final Page<Offer> expected = new PageImpl<>(List.of(Fixtures.offer));
            when(offerRepository.findAllByStatusIn(page, statusQuery.getStatuses())).thenReturn(expected);

            assertEquals(expected, service.findBy(page, user, statusQuery, tagQuery, searchQuery));
        }

        @Test
        void findAllByTagIn() {
            final var page = Pageable.unpaged();
            final var user = new UserId();
            final var statusQuery = new OfferStatusQuery("OPEN");
            final var tagQuery = new OfferTagQuery("tag1;tag2");
            final var searchQuery = new OfferSearchQuery("");

            final Page<Offer> expected = new PageImpl<>(List.of(Fixtures.offer));
            when(offerRepository.findAllByStatusInAndTagsIn(page, statusQuery.getStatuses(), tagQuery.getTags())).thenReturn(expected);

            assertEquals(expected, service.findBy(page, user, statusQuery, tagQuery, searchQuery));
        }

        @Test
        void findAllByUser() {
            final var page = Pageable.unpaged();
            final var user = new UserId("user");
            final var statusQuery = new OfferStatusQuery("");
            final var tagQuery = new OfferTagQuery("");
            final var searchQuery = new OfferSearchQuery("");

            final Page<Offer> expected = new PageImpl<>(List.of(Fixtures.offer));
            when(offerRepository.findAllByUserId(page, user)).thenReturn(expected);

            assertEquals(expected, service.findBy(page, user, statusQuery, tagQuery, searchQuery));
        }

        @Test
        void findAllByUserAndStatus() {
            final var page = Pageable.unpaged();
            final var user = new UserId("user");
            final var statusQuery = new OfferStatusQuery("DRAFT;CLOSED;OPEN");
            final var tagQuery = new OfferTagQuery("");
            final var searchQuery = new OfferSearchQuery("");

            final Page<Offer> expected = new PageImpl<>(List.of(Fixtures.offer));
            when(offerRepository.findAllByUserIdAndStatusIn(page, user, statusQuery.getStatuses())).thenReturn(expected);

            assertEquals(expected, service.findBy(page, user, statusQuery, tagQuery, searchQuery));
        }

        @Test
        void findAllByUserAndTags() {
            final var page = Pageable.unpaged();
            final var user = new UserId("user");
            final var statusQuery = new OfferStatusQuery("OPEN");
            final var tagQuery = new OfferTagQuery("tag1;tag2;");
            final var searchQuery = new OfferSearchQuery("");

            final Page<Offer> expected = new PageImpl<>(List.of(Fixtures.offer));
            when(offerRepository.findAllByUserIdAndStatusInAndTagsIn(page, user, statusQuery.getStatuses(), tagQuery.getTags())).thenReturn(expected);

            assertEquals(expected, service.findBy(page, user, statusQuery, tagQuery, searchQuery));
        }

        @Test
        void findAllByUserStatusAndTags() {
            final var page = Pageable.unpaged();
            final var user = new UserId("user");
            final var statusQuery = new OfferStatusQuery("OPEN;CLOSED");
            final var tagQuery = new OfferTagQuery("tag1;tag2;");
            final var searchQuery = new OfferSearchQuery("");

            final Page<Offer> expected = new PageImpl<>(List.of(Fixtures.offer));
            when(offerRepository.findAllByUserIdAndStatusInAndTagsIn(page, user, statusQuery.getStatuses(), tagQuery.getTags())).thenReturn(expected);

            assertEquals(expected, service.findBy(page, user, statusQuery, tagQuery, searchQuery));
        }
    }

    @Nested
    class FindOpenTests {
        @Test
        void findOpen() {
            final var page = Pageable.unpaged();
            final var user = new UserId("user");
            final var statusQuery = new OfferStatusQuery("OPEN");
            final var tagQuery = new OfferTagQuery("tag1;tag2;");
            final var searchQuery = new OfferSearchQuery("");

            final Page<Offer> expected = new PageImpl<>(List.of(Fixtures.offer));
            when(offerRepository.findAllByUserIdAndStatusInAndTagsIn(page, user, statusQuery.getStatuses(), tagQuery.getTags())).thenReturn(expected);

            assertEquals(expected, service.findOpen(page, user, tagQuery, searchQuery));
        }
    }
}
