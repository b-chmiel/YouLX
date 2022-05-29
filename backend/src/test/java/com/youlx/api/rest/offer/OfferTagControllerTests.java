package com.youlx.api.rest.offer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youlx.api.Routes;
import com.youlx.api.config.SecurityConfig;
import com.youlx.api.rest.tag.TagDto;
import com.youlx.domain.tag.TagService;
import com.youlx.domain.utils.exception.ApiConflictException;
import com.youlx.domain.utils.exception.ApiCustomException;
import com.youlx.domain.utils.exception.ApiNotFoundException;
import com.youlx.domain.utils.exception.ApiUnauthorizedException;
import com.youlx.testUtils.MvcHelpers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.*;
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
            doThrow(new ApiUnauthorizedException("")).when(service).assignToOffer(null, offerId, tag.toDomain());
            helpers.postRequest(tag, Routes.Offer.OFFERS + "/" + offerId + "/tag").andExpect(status().isForbidden());
        }

        @Test
        void notFound() throws Exception {
            final var offerId = "id";
            final var tag = new TagDto("asdf");
            doThrow(new ApiNotFoundException("")).when(service).assignToOffer(null, offerId, tag.toDomain());
            helpers.postRequest(tag, Routes.Offer.OFFERS + "/" + offerId + "/tag").andExpect(status().isNotFound());
        }

        @Test
        void conflict() throws Exception {
            final var offerId = "id";
            final var tag = new TagDto("asdf");
            doThrow(new ApiConflictException("")).when(service).assignToOffer(null, offerId, tag.toDomain());
            helpers.postRequest(tag, Routes.Offer.OFFERS + "/" + offerId + "/tag").andExpect(status().isConflict());
        }

        @Test
        void badRequest() throws Exception {
            final var offerId = "id";
            final var tag = new TagDto("asdf");
            doThrow(new ApiCustomException("")).when(service).assignToOffer(null, offerId, tag.toDomain());
            helpers.postRequest(tag, Routes.Offer.OFFERS + "/" + offerId + "/tag").andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser("user")
        void assign() throws Exception {
            final var offerId = "id";
            final var tag = new TagDto("asdf");
            helpers.postRequest(tag, Routes.Offer.OFFERS + "/" + offerId + "/tag").andExpect(status().isOk());
            verify(service, times(1)).assignToOffer("user", offerId, tag.toDomain());
        }
    }
}
