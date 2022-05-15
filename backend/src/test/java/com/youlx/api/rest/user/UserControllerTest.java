package com.youlx.api.rest.user;

import com.youlx.api.Routes;
import com.youlx.testUtils.MvcHelpers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MvcHelpers helpers;

    private final String mockUserLogin = "test-user";

    @Nested
    public class MeTests {
        @Test
        public void profileReturns403IfUserIsNotAuthenticated() throws Exception {
            helpers.getRequest(Routes.User.ME).andDo(print()).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(mockUserLogin)
        public void profileReturnsUserInfo() throws Exception {
            final var result = helpers.getRequest(Routes.User.ME);

            result.andExpect(status().isOk());
            assertEquals(MvcHelpers.attributeFromResult("login", result), mockUserLogin);
        }
    }

    @Nested
    public class OfferTests {
        @Test
        public void shouldReturn401WhenNotAuthenticated() throws Exception {
            helpers.getRequest(Routes.User.USER + "/123/offers").andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        public void shouldReturn404OnNonExistingUser() throws Exception {
            helpers.getRequest(Routes.User.USER + "/123/offers").andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        public void shouldReturn200ForAdmin() throws Exception {
            helpers.getRequest(Routes.User.USER + "/admin/offers").andExpect(status().isOk());
        }
    }
}
