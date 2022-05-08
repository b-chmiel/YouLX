package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.testUtils.MvcHelpers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

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

    @Test
    public void getAccessibleForUnauthenticatedUser() throws Exception {
        commonHelpers.getRequest(Routes.Offer.OFFERS).andDo(print()).andExpect(status().isOk());
    }

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
        assertEquals(MvcHelpers.attributeFromResult("name", created), name);
        assertEquals(MvcHelpers.attributeFromResult("description", created), desc);
    }
}