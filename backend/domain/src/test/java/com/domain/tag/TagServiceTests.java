package com.domain.tag;

import com.domain.offer.stateCheck.OfferStateCheckService;
import com.domain.user.UserId;
import com.domain.utils.exception.ApiUnauthorizedException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class TagServiceTests {
    private final TagRepository repository = mock(TagRepository.class);
    private final TagSearchRepository searchRepository = mock(TagSearchRepository.class);
    private final OfferStateCheckService offerStateCheckService = mock(OfferStateCheckService.class);
    private final TagService service = new TagServiceImpl(repository, searchRepository);

    @Nested
    class GetAllTests {
        @Test
        void getAll() {
            service.getAll("");
            verify(repository, times(1)).getAll();
        }

        @Test
        void getAllQuery() {
            final var query = "query";
            service.getAll(query);
            verify(searchRepository, times(1)).search(query);
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
}
