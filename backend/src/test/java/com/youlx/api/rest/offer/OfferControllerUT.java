package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferClose;
import com.youlx.domain.offer.OfferCloseReason;
import com.youlx.domain.offer.OfferService;
import com.youlx.domain.user.User;
import com.youlx.domain.utils.exception.ApiCustomException;
import com.youlx.domain.utils.exception.ApiNotFoundException;
import com.youlx.testUtils.MvcHelpers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OfferControllerUT {
    @Autowired
    private MvcHelpers helpers;

    @MockBean
    private OfferService service;

    private static final String url = Routes.Offer.OFFERS;

    @Nested
    class CloseTests {
        @Test
        void unauthorized() throws Exception {
            helpers.postRequest(null, url + "/123/close").andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser("user")
        void close() throws Exception {
            final var id = "a";
            final var user = new User(List.of(), "", "", "", "", "user", "");
            final var offer = new Offer(null, null, user, null);
            when(service.close(id, new OfferClose(OfferCloseReason.MANUAL), "user")).thenReturn(offer);

            helpers.postRequest(null, url + "/" + id + "/close").andExpect(status().isOk());
        }

        @Test
        @WithMockUser("user")
        void notFound() throws Exception {
            final var id = "a";
            doThrow(new ApiNotFoundException("")).when(service).close(id, new OfferClose(OfferCloseReason.MANUAL), "user");

            helpers.postRequest(null, url + "/" + id + "/close").andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser("user")
        void otherError() throws Exception {
            final var id = "a";
            doThrow(new ApiCustomException("")).when(service).close(id, new OfferClose(OfferCloseReason.MANUAL), "user");

            helpers.postRequest(null, url + "/" + id + "/close").andExpect(status().isBadRequest());
        }
    }
}
