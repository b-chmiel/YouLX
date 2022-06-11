package com.api.rest.tag;

import com.api.MvcHelpers;
import com.api.Routes;
import com.domain.tag.Tag;
import com.domain.tag.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.when;
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
            final var query = "query";
            when(service.getAll(query)).thenReturn(tags);
            final var result = helpers.getRequest(Routes.Tag.TAG + "?query=" + query).andExpect(status().isOk());
            Assertions.assertEquals("{name=a}", MvcHelpers.attributeFromResult("[0]", result));
            Assertions.assertEquals("{name=b}", MvcHelpers.attributeFromResult("[1]", result));
            Assertions.assertEquals("{name=c}", MvcHelpers.attributeFromResult("[2]", result));
        }
    }
}
