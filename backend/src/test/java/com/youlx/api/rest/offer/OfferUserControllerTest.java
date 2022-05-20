package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.testUtils.MvcHelpers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OfferUserControllerTest {
    @Autowired
    private MvcHelpers helpers;

    @Nested
    class OfferTests {
        @Test
        void shouldReturn403WhenNotAuthenticated() throws Exception {
            helpers.getRequest(Routes.User.USER + "/123/offers").andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        void shouldReturn404OnNonExistingUser() throws Exception {
            helpers.getRequest(Routes.User.USER + "/123/offers").andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void shouldReturn200ForAdmin() throws Exception {
            helpers.getRequest(Routes.User.USER + "/admin/offers").andExpect(status().isOk());
        }
    }
}
