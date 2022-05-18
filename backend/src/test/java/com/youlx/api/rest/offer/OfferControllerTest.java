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
public class OfferControllerTest {
    @Autowired
    private MvcHelpers commonHelpers;

    @Autowired
    private UserRepository userRepository;

    private final static User mockUser = new User(List.of(), "", "", "", "", "a");

    @BeforeEach
    void setup() {
        userRepository.create(mockUser);
    }

    @Nested
    public class GetTests {
        @Test
        @WithMockUser(value = "a")
        public void shouldGetRecentlyCreated() throws Exception {
            final var offer = new OfferDto(new Offer("", "", mockUser));

            final var response = commonHelpers.postRequest(offer, Routes.Offer.OFFERS);
            final var location = response.andReturn().getResponse().getHeader("location");
            commonHelpers.getRequest(location).andExpect(status().isOk());
        }

        @Test
        @WithMockUser("a")
        public void shouldShowHateoasCloseOnOfferOwner() throws Exception {
            final var offer = new OfferDto(new Offer("", "", mockUser));

            final var response = commonHelpers.postRequest(offer, Routes.Offer.OFFERS);
            final var location = response.andReturn().getResponse().getHeader("location");
            final var result = commonHelpers.getRequest(location);

            assertThat(MvcHelpers.attributeFromResult("_links.self[0].href", result), containsString("/close"));
        }
    }

    @Nested
    public class GetAllTests {
        @Test
        public void accessibleForUnauthenticatedUser() throws Exception {
            final var response = commonHelpers.getRequest(Routes.Offer.OFFERS).andDo(print()).andExpect(status().isOk());

            assertThat(MvcHelpers.attributeFromResult("_embedded.offers[0]._links.self.href", response), not(containsString("/close")));
        }

        @Test
        @WithMockUser()
        public void accessibleForAuthenticatedNotRegisteredUser() throws Exception {
            final var response = commonHelpers.getRequest(Routes.Offer.OFFERS).andDo(print()).andExpect(status().isOk());

            assertThat(MvcHelpers.attributeFromResult("_embedded.offers[0]._links.self.href", response), not(containsString("/close")));
        }
    }

    @Nested
    public class CreateTests {
        @Test
        @WithMockUser("a")
        public void create() throws Exception {
            final var name = "asdf";
            final var desc = "fdsa";
            final var offer = new OfferCreateDto(name, desc);

            final var response = commonHelpers.postRequest(offer, Routes.Offer.OFFERS);

            response.andExpect(status().isCreated());

            final var location = response.andReturn().getResponse().getHeader("location");
            assertTrue(Objects.requireNonNull(response.andReturn().getResponse().getHeader("location")).contains(Routes.Offer.OFFERS));

            final var created = commonHelpers.getRequest(location);
            assertEquals(name, MvcHelpers.attributeFromResult("name", created));
            assertEquals(desc, MvcHelpers.attributeFromResult("description", created));
            assertEquals(OfferStatus.OPEN.name(), MvcHelpers.attributeFromResult("status", created));

            final var expectedDate = LocalDateTime.now();
            final var actualDate = LocalDateTime.parse(MvcHelpers.attributeFromResult("creationDate", created));

            assertTrue(Math.abs(expectedDate.toEpochSecond(ZoneOffset.UTC) - actualDate.toEpochSecond(ZoneOffset.UTC)) < 2);
        }
    }

    @Nested
    public class CloseTests {
        @Test
        @WithMockUser("a")
        public void shouldCloseOffer() throws Exception {
            final var offer = new OfferDto(new Offer("", "", mockUser));

            final var response = commonHelpers.postRequest(offer, Routes.Offer.OFFERS);
            final var location = response.andReturn().getResponse().getHeader("location");
            commonHelpers.postRequest(new OfferCloseDto(OfferCloseReason.EXPIRED), location + "/close");
            final var result = commonHelpers.getRequest(location);
            assertEquals(OfferCloseReason.EXPIRED.name(), MvcHelpers.attributeFromResult("closeReason", result));
        }
    }
}