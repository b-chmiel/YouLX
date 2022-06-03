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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OfferFindServiceTests {
    private final OfferRepository offerRepository = mock(OfferRepository.class);
    private final OfferSearchRepository offerSearchRepository = mock(OfferSearchRepository.class);
    private final OfferStateCheckService offerStateCheckService = mock(OfferStateCheckService.class);
    private final OfferFindRepository offerFindRepository = mock(OfferFindRepository.class);
    private final OfferFindService service = new OfferFindServiceImpl(offerRepository, offerSearchRepository, offerStateCheckService, offerFindRepository);

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
        void findAll() {
            final var page = Pageable.unpaged();
            final var user = new UserId();
            final var statusQuery = new OfferStatusQuery("OPEN");
            final var tagQuery = new OfferTagQuery("");

            final Page<Offer> expected = new PageImpl<>(List.of(Fixtures.offer));
            when(offerFindRepository.findAllByStatusIn(page, statusQuery.getStatuses())).thenReturn(expected);

            assertEquals(expected, service.findBy(page, user, statusQuery, tagQuery));
        }

        @Test
        void findAllByTagIn() {
            final var page = Pageable.unpaged();
            final var user = new UserId();
            final var statusQuery = new OfferStatusQuery("OPEN");
            final var tagQuery = new OfferTagQuery("tag1;tag2");

            final Page<Offer> expected = new PageImpl<>(List.of(Fixtures.offer));
            when(offerFindRepository.findAllByStatusInAndTagsIn(page, statusQuery.getStatuses(), tagQuery.getTags())).thenReturn(expected);

            assertEquals(expected, service.findBy(page, user, statusQuery, tagQuery));
        }

        @Test
        void findAllByUser() {
            final var page = Pageable.unpaged();
            final var user = new UserId("user");
            final var statusQuery = new OfferStatusQuery("");
            final var tagQuery = new OfferTagQuery("");

            final Page<Offer> expected = new PageImpl<>(List.of(Fixtures.offer));
            when(offerFindRepository.findAllByUserId(page, user)).thenReturn(expected);

            assertEquals(expected, service.findBy(page, user, statusQuery, tagQuery));
        }

        @Test
        void findAllByUserAndStatus() {
            final var page = Pageable.unpaged();
            final var user = new UserId("user");
            final var statusQuery = new OfferStatusQuery("DRAFT;CLOSED;OPEN");
            final var tagQuery = new OfferTagQuery("");

            final Page<Offer> expected = new PageImpl<>(List.of(Fixtures.offer));
            when(offerFindRepository.findAllByUserIdAndStatusIn(page, user, statusQuery.getStatuses())).thenReturn(expected);

            assertEquals(expected, service.findBy(page, user, statusQuery, tagQuery));
        }

        @Test
        void findAllByUserAndTags() {
            final var page = Pageable.unpaged();
            final var user = new UserId("user");
            final var statusQuery = new OfferStatusQuery("OPEN");
            final var tagQuery = new OfferTagQuery("tag1;tag2;");

            final Page<Offer> expected = new PageImpl<>(List.of(Fixtures.offer));
            when(offerFindRepository.findAllByUserIdAndStatusInAndTagsIn(page, user, statusQuery.getStatuses(), tagQuery.getTags())).thenReturn(expected);

            assertEquals(expected, service.findBy(page, user, statusQuery, tagQuery));
        }

        @Test
        void findAllByUserStatusAndTags() {
            final var page = Pageable.unpaged();
            final var user = new UserId("user");
            final var statusQuery = new OfferStatusQuery("OPEN;CLOSED");
            final var tagQuery = new OfferTagQuery("tag1;tag2;");

            final Page<Offer> expected = new PageImpl<>(List.of(Fixtures.offer));
            when(offerFindRepository.findAllByUserIdAndStatusInAndTagsIn(page, user, statusQuery.getStatuses(), tagQuery.getTags())).thenReturn(expected);

            assertEquals(expected, service.findBy(page, user, statusQuery, tagQuery));
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

            final Page<Offer> expected = new PageImpl<>(List.of(Fixtures.offer));
            when(offerFindRepository.findAllByUserIdAndStatusInAndTagsIn(page, user, statusQuery.getStatuses(), tagQuery.getTags())).thenReturn(expected);

            assertEquals(expected, service.findOpen(page, user, tagQuery));
        }
    }

    @Nested
    class SearchTests {
        @Test
        void noOffersFound() {
            final var query = "asdf";

            when(offerSearchRepository.search(query)).thenReturn(List.of());

            assertEquals(List.of(), service.search(new UserId(Fixtures.user.getUsername()), query));
        }

        @Test
        void notVisible() {
            final var query = "asdf";

            when(offerSearchRepository.search(query)).thenReturn(List.of(Fixtures.offer));
            when(offerStateCheckService.isVisible(new UserId(Fixtures.user.getUsername()), Fixtures.offer)).thenReturn(false);

            assertEquals(List.of(), service.search(new UserId(Fixtures.user.getUsername()), query));
        }

        @Test
        void search() {
            final var query = "asdf";

            when(offerSearchRepository.search(query)).thenReturn(List.of(Fixtures.offer));
            when(offerStateCheckService.isVisible(new UserId(Fixtures.user.getUsername()), Fixtures.offer)).thenReturn(true);

            assertEquals(List.of(Fixtures.offer), service.search(new UserId(Fixtures.user.getUsername()), query));

            verify(offerSearchRepository, times(1)).search(query);
        }
    }
}
