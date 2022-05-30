package com.youlx.api.rest.tag;

import com.youlx.api.Routes;
import com.youlx.domain.tag.Tag;
import com.youlx.domain.tag.TagService;
import com.youlx.domain.utils.exception.ApiConflictException;
import com.youlx.testUtils.MvcHelpers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TagControllerTests {
    @MockBean
    private TagService service;

    @Autowired
    private MvcHelpers helpers;

    @Nested
    class GetAllTests {
        @Test
        void getAll() throws Exception {
            final var tags = List.of(new Tag("a"), new Tag("b"), new Tag("c"));
            when(service.getAll()).thenReturn(tags);
            final var result = helpers.getRequest(Routes.Tag.TAG).andExpect(status().isOk());
            assertEquals("{name=a}", MvcHelpers.attributeFromResult("[0]", result));
            assertEquals("{name=b}", MvcHelpers.attributeFromResult("[1]", result));
            assertEquals("{name=c}", MvcHelpers.attributeFromResult("[2]", result));
        }
    }

    @Nested
    class CreateTests {
        @Test
        void forbidden() throws Exception {
            helpers.postRequest(new TagDto("a"), Routes.Tag.TAG).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        void conflict() throws Exception {
            final var tag = new Tag("asdf");
            doThrow(new ApiConflictException("")).when(service).create(tag);
            helpers.postRequest(tag, Routes.Tag.TAG).andExpect(status().isConflict());
        }

        @Test
        @WithMockUser
        void create() throws Exception {
            final var tag = new Tag("asdf");
            helpers.postRequest(tag, Routes.Tag.TAG).andExpect(status().isCreated());
            verify(service, times(1)).create(tag);
        }
    }
}
