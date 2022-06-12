package com.api.rest.offer;

import com.api.MvcHelpers;
import com.api.Routes;
import com.api.rest.tag.TagDto;
import com.domain.offer.Offer;
import com.domain.offer.OfferStatus;
import com.domain.offer.modify.OfferCloseReason;
import com.domain.tag.Tag;
import com.domain.user.User;
import com.domain.user.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan(basePackages = "com.infrastructure")
@ActiveProfiles(profiles = "local")
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
            final var offer = new OfferDto(new Offer("", "", mockUser, null, Set.of(new Tag("string"))));

            final var response = helpers.postRequest(offer, Routes.Offer.OFFERS);
            final var location = response.andReturn().getResponse().getHeader("location");
            helpers.getRequest(location).andExpect(status().isOk());
        }

        @Test
        @WithMockUser("a")
        void publishForNewlyCreatedOfferForOwner() throws Exception {
            final var offer = new OfferDto(new Offer("", "", mockUser, null, Set.of(new Tag("string"))));

            final var response = helpers.postRequest(offer, Routes.Offer.OFFERS);
            final var location = response.andReturn().getResponse().getHeader("location");
            final var result = helpers.getRequest(location);

            assertThat(MvcHelpers.attributeFromResult("_links.publish.href", result), containsString("/publish"));
        }

        @Test
        @WithMockUser("a")
        void closeForPublishedOffer() throws Exception {
            final var offer = new OfferDto(new Offer("", "", mockUser, null, Set.of(new Tag("string"))));
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
            final var offer = new OfferCreateDto(name, desc, price, Set.of(new TagDto("tags")));

            final var response = helpers.postRequest(offer, Routes.Offer.OFFERS);

            response.andExpect(status().isCreated());

            final var location = response.andReturn().getResponse().getHeader("location");
            Assertions.assertTrue(Objects.requireNonNull(response.andReturn().getResponse().getHeader("location")).contains(Routes.Offer.OFFERS));

            final var created = helpers.getRequest(location);
            Assertions.assertEquals(name, MvcHelpers.attributeFromResult("name", created));
            Assertions.assertEquals(desc, MvcHelpers.attributeFromResult("description", created));
            assertThat(price, Matchers.comparesEqualTo(new BigDecimal(MvcHelpers.attributeFromResult("price", created))));
            assertEquals(OfferStatus.DRAFT.name(), MvcHelpers.attributeFromResult("status", created));
            assertEquals(offer.getTags().stream().toList().get(0).getName(), MvcHelpers.attributeFromResult("tags.[0].name", created));

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
            final var offer = new OfferDto(new Offer("", "", mockUser, null, Set.of(new Tag("string"))));

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
            final var offer = new OfferCreateDto("", "", null, Set.of(new TagDto("tag")));
            final var id = "a";
            final var url = Routes.Offer.OFFERS + '/' + id;
            helpers.putRequest(offer, url).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        void notFound() throws Exception {
            final var offer = new OfferCreateDto("", "", null, Set.of(new TagDto("tag")));
            final var id = "a";
            final var url = Routes.Offer.OFFERS + '/' + id;

            helpers.putRequest(offer, url).andExpect(status().isNotFound());
        }
    }
}