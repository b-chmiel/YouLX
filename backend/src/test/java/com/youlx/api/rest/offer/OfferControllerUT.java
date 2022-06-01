package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.offer.*;
import com.youlx.domain.user.User;
import com.youlx.domain.user.UserId;
import com.youlx.domain.user.UserService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.youlx.testUtils.Fixtures.offer;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OfferControllerUT {
    @Autowired
    private MvcHelpers helpers;

    @MockBean
    private OfferModifyService service;
    @MockBean
    private OfferFindService offerFindService;
    @MockBean
    private OfferStateCheckService offerStateCheckService;
    @MockBean
    private UserService userService;

    private static final String url = Routes.Offer.OFFERS;

    @Nested
    class CreateTests {
        @Test
        void notAuthorized() throws Exception {
            final var offer = new OfferCreateDto("name", "desc", BigDecimal.TEN);
            helpers.postRequest(offer, Routes.Offer.OFFERS).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        void userNotFound() throws Exception {
            final var offerCreate = new OfferCreateDto("name", "desc", BigDecimal.TEN);
            final var user = new User(List.of(), "", "", "", "", "user", "");

            when(userService.findById(user.getUsername())).thenReturn(Optional.empty());

            helpers.postRequest(offerCreate, Routes.Offer.OFFERS).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser("user")
        void create() throws Exception {
            final var offerCreate = new OfferCreateDto("name", "desc", BigDecimal.TEN);
            final var user = new User(List.of(), "", "", "", "", "user", "");
            final var offer = offerCreate.toDomain(user);

            when(userService.findById(user.getUsername())).thenReturn(Optional.of(user));
            when(service.create(any(Offer.class))).thenReturn(offer);

            helpers.postRequest(offerCreate, Routes.Offer.OFFERS).andExpect(status().isCreated());
        }
    }

    @Nested
    class GetTests {
        @Test
        void get() throws Exception {
            final var id = "a";
            when(offerFindService.findById(id)).thenReturn(Optional.of(offer));
            when(offerStateCheckService.isVisible(new UserId(), id)).thenReturn(true);

            helpers.getRequest(Routes.Offer.OFFERS + "/" + id).andExpect(status().isOk());
        }

        @Test
        @WithMockUser("user")
        void getAuthenticated() throws Exception {
            final var id = "a";
            when(offerFindService.findById(id)).thenReturn(Optional.of(offer));
            when(offerStateCheckService.isVisible(new UserId("user"), id)).thenReturn(true);
            helpers.getRequest(Routes.Offer.OFFERS + "/" + id).andExpect(status().isOk());
        }

        @Test
        void notFound() throws Exception {
            final var id = "a";
            when(offerStateCheckService.isVisible(new UserId(), id)).thenReturn(false);
            helpers.getRequest(Routes.Offer.OFFERS + "/" + id).andExpect(status().isNotFound());
        }

        @Test
        void offerNotFound() throws Exception {
            final var id = "a";
            when(offerFindService.findById(id)).thenReturn(Optional.empty());
            when(offerStateCheckService.isVisible(new UserId(), id)).thenReturn(true);
            helpers.getRequest(Routes.Offer.OFFERS + "/" + id).andExpect(status().isNotFound());
        }
    }

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
            when(service.close(id, new OfferClose(OfferCloseReason.MANUAL), new UserId("user"))).thenReturn(offer);

            helpers.postRequest(null, url + "/" + id + "/close").andExpect(status().isOk());
        }

        @Test
        @WithMockUser("user")
        void notFound() throws Exception {
            final var id = "a";
            doThrow(new ApiNotFoundException("")).when(service).close(id, new OfferClose(OfferCloseReason.MANUAL), new UserId("user"));

            helpers.postRequest(null, url + "/" + id + "/close").andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser("user")
        void otherError() throws Exception {
            final var id = "a";
            doThrow(new ApiCustomException("")).when(service).close(id, new OfferClose(OfferCloseReason.MANUAL), new UserId("user"));

            helpers.postRequest(null, url + "/" + id + "/close").andExpect(status().isBadRequest());
        }
    }

    @Nested
    class ModifyTests {
        @Test
        void notAuthenticated() throws Exception {
            helpers.putRequest(new OfferCreateDto(), Routes.Offer.OFFERS + "/asdf").andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        void modify() throws Exception {
            final var id = "a";
            final var offer = new OfferCreateDto();
            helpers.putRequest(new OfferCreateDto(), Routes.Offer.OFFERS + "/" + id).andExpect(status().isOk());
            verify(service, times(1)).modify(id, new OfferModify(offer.getName(), offer.getDescription(), offer.getPrice()), new UserId("user"));
        }
    }

    @Nested
    class PublishTests {
        @Test
        void notAuthenticated() throws Exception {
            helpers.postRequest(null, Routes.Offer.OFFERS + "/asdf/publish").andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser("user")
        void publish() throws Exception {
            final var id = "a";
            helpers.postRequest(null, Routes.Offer.OFFERS + "/" + id + "/publish").andExpect(status().isOk());
            verify(service, times(1)).publish(new UserId("user"), id);
        }
    }

    @Nested
    class SearchTests {
        @Test
        void search() throws Exception {
            final var query = "asdf";
            helpers.getRequest(Routes.Offer.OFFERS + "/search?query=" + query).andExpect(status().isOk());

            verify(offerFindService).search(new UserId(), query);
        }
    }
}
