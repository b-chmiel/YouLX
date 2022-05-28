package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferCloseReason;
import com.youlx.domain.offer.OfferStatus;
import com.youlx.domain.user.User;
import com.youlx.domain.user.UserRepository;
import com.youlx.testUtils.MvcHelpers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OfferControllerFT {
    @Autowired
    private MvcHelpers helpers;

    @Autowired
    private UserRepository userRepository;

    private final static User mockUser = new User(List.of(), "", "", "", "", "a", "");

    @BeforeEach
    void setup() {
        userRepository.create(mockUser);
    }

    @Nested
    class GetTests {
        @Test
        @WithMockUser(value = "a")
        void shouldGetRecentlyCreated() throws Exception {
            final var offer = new OfferDto(new Offer("", "", mockUser, null));

            final var response = helpers.postRequest(offer, Routes.Offer.OFFERS);
            final var location = response.andReturn().getResponse().getHeader("location");
            helpers.getRequest(location).andExpect(status().isOk());
        }

        @Test
        @WithMockUser("a")
        void publishForNewlyCreatedOfferForOwner() throws Exception {
            final var offer = new OfferDto(new Offer("", "", mockUser, null));

            final var response = helpers.postRequest(offer, Routes.Offer.OFFERS);
            final var location = response.andReturn().getResponse().getHeader("location");
            final var result = helpers.getRequest(location);

            assertThat(MvcHelpers.attributeFromResult("_links.publish.href", result), containsString("/publish"));
        }

        @Test
        @WithMockUser("a")
        void closeForPublishedOffer() throws Exception {
            final var offer = new OfferDto(new Offer("", "", mockUser, null));
            final var response = helpers.postRequest(offer, Routes.Offer.OFFERS);
            final var location = response.andReturn().getResponse().getHeader("location");
            helpers.postRequest(null, location + "/publish");

            final var result = helpers.getRequest(location);

            assertThat(MvcHelpers.attributeFromResult("_links.close.href", result), containsString("/close"));
        }
    }

    @Nested
    class GetAllTests {
        @Test
        void accessibleForUnauthenticatedUser() throws Exception {
            final var response = helpers.getRequest(Routes.Offer.OFFERS).andDo(print()).andExpect(status().isOk());

            assertThat(MvcHelpers.attributeFromResult("_embedded.offers[0]._links", response), not(containsString("/close")));
            assertThat(MvcHelpers.attributeFromResult("_embedded.offers[0]._links", response), not(containsString("/publish")));
        }

        @Test
        @WithMockUser()
        void accessibleForAuthenticatedNotRegisteredUser() throws Exception {
            final var response = helpers.getRequest(Routes.Offer.OFFERS).andDo(print()).andExpect(status().isOk());

            assertThat(MvcHelpers.attributeFromResult("_embedded.offers[0]._links.self.href", response), not(containsString("/close")));
        }
    }

    @Nested
    class CreateTests {
        @Test
        @WithMockUser("a")
        void create() throws Exception {
            final var name = "asdf";
            final var desc = "fdsa";
            final var price = BigDecimal.TEN;
            final var offer = new OfferCreateDto(name, desc, price);

            final var response = helpers.postRequest(offer, Routes.Offer.OFFERS);

            response.andExpect(status().isCreated());

            final var location = response.andReturn().getResponse().getHeader("location");
            assertTrue(Objects.requireNonNull(response.andReturn().getResponse().getHeader("location")).contains(Routes.Offer.OFFERS));

            final var created = helpers.getRequest(location);
            assertEquals(name, MvcHelpers.attributeFromResult("name", created));
            assertEquals(desc, MvcHelpers.attributeFromResult("description", created));
            assertEquals(price, BigDecimal.valueOf(Long.parseLong(MvcHelpers.attributeFromResult("price", created))));
            assertEquals(OfferStatus.DRAFT.name(), MvcHelpers.attributeFromResult("status", created));

            final var expectedDate = LocalDateTime.now();
            final var actualDate = LocalDateTime.parse(MvcHelpers.attributeFromResult("creationDate", created));

            assertTrue(Math.abs(expectedDate.toEpochSecond(ZoneOffset.UTC) - actualDate.toEpochSecond(ZoneOffset.UTC)) < 2);
        }
    }

    @Nested
    class CloseTests {
        @Test
        @WithMockUser("a")
        void shouldCloseOffer() throws Exception {
            final var offer = new OfferDto(new Offer("", "", mockUser, null));

            final var response = helpers.postRequest(offer, Routes.Offer.OFFERS).andExpect(status().isCreated());
            final var location = response.andReturn().getResponse().getHeader("location");
            helpers.postRequest(null, location + "/publish").andExpect(status().isOk());
            helpers.postRequest(null, location + "/close").andExpect(status().isOk());
            final var result = helpers.getRequest(location).andExpect(status().isOk());
            assertEquals(OfferCloseReason.MANUAL.name(), MvcHelpers.attributeFromResult("closeReason", result));
        }
    }

    @Nested
    class ModifyTests {
        @Test
        void unauthorized() throws Exception {
            final var offer = new OfferCreateDto("", "", null);
            final var id = "a";
            final var url = Routes.Offer.OFFERS + '/' + id;
            helpers.putRequest(offer, url).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        void notFound() throws Exception {
            final var offer = new OfferCreateDto("", "", null);
            final var id = "a";
            final var url = Routes.Offer.OFFERS + '/' + id;

            helpers.putRequest(offer, url).andExpect(status().isNotFound());
        }
    }
}