package com.youlx.domain.tag;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;


class TagServiceTests {
    private final TagRepository repository = mock(TagRepository.class);
    private final TagService service = new TagServiceImpl(repository);

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
}
