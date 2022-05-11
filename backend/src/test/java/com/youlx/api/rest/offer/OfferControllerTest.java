package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferStatus;
import com.youlx.testUtils.MvcHelpers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OfferControllerTest {
    @Autowired
    private MvcHelpers commonHelpers;

    @Nested
    public class GetTests {
        @Test
        @WithMockUser
        public void shouldGetRecentlyCreated() throws Exception {
            final var name = "asdf";
            final var description = "fdsa";
            final var offer = new OfferDto(new Offer(name, description, "user"));

            final var response = commonHelpers.postRequest(offer, Routes.Offer.OFFERS);
            final var location = response.andReturn().getResponse().getHeader("location");
            commonHelpers.getRequest(location).andExpect(status().isOk());
        }
    }

    @Nested
    public class GetAllTests {
        @Test
        public void accessibleForUnauthenticatedUser() throws Exception {
            commonHelpers.getRequest(Routes.Offer.OFFERS).andDo(print()).andExpect(status().isOk());
        }
    }

    @Nested
    public class CreateTests {
        @Test
        @WithMockUser
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

            assertEquals(expectedDate.toEpochSecond(ZoneOffset.UTC) ,actualDate.toEpochSecond(ZoneOffset.UTC));
        }
    }
}