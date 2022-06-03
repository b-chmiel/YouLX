package com.api.rest.offer;

import com.api.MvcHelpers;
import com.api.Routes;
import com.api.rest.tag.TagDto;
import com.domain.tag.TagService;
import com.domain.user.UserId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OfferTagControllerTests {
    @Autowired
    private MvcHelpers helpers;

    @MockBean
    private TagService service;

    @Nested
    class AssignTests {
        @Test
        void unauthorized() throws Exception {
            final var offerId = "id";
            final var tag = new TagDto("asdf");
            helpers.postRequest(tag, Routes.Offer.OFFERS + "/" + offerId + "/tag").andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser("user")
        void assign() throws Exception {
            final var offerId = "id";
            final var tag = new TagDto("asdf");
            helpers.postRequest(tag, Routes.Offer.OFFERS + "/" + offerId + "/tag").andExpect(status().isOk());
            verify(service, times(1)).assignToOffer(new UserId("user"), offerId, tag.toDomain());
        }
    }
}
