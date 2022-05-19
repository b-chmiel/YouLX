package com.youlx.api.rest.user;

import com.youlx.api.Routes;
import com.youlx.domain.offer.OfferRepository;
import com.youlx.domain.user.User;
import com.youlx.domain.user.UserRepository;
import com.youlx.testUtils.MvcHelpers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MvcHelpers helpers;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferRepository offerRepository;

    private final String mockUserLogin = "test-user";

    @Nested
    public class MeTests {
        @Test
        public void profileReturns403IfUserIsNotAuthenticated() throws Exception {
            helpers.getRequest(Routes.User.ME).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(mockUserLogin)
        public void profileReturnsNotFoundForNonExistingUser() throws Exception {
            offerRepository.clear();
            userRepository.clear();

            final var result = helpers.getRequest(Routes.User.ME);
            result.andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser("e")
        public void profileReturnsInfoOfUser() throws Exception {
            final var user = new User(List.of(), "a", "b", "c", "d", "e");
            userRepository.create(user);

            final var result = helpers.getRequest(Routes.User.ME);
            result.andExpect(status().isOk());
            assertEquals(user.getFirstName(), MvcHelpers.attributeFromResult("firstName", result));
            assertEquals(user.getLastName(), MvcHelpers.attributeFromResult("lastName", result));
            assertEquals(user.getEmail(), MvcHelpers.attributeFromResult("email", result));
            assertEquals(user.getUsername(), MvcHelpers.attributeFromResult("login", result));
        }
    }

    @Nested
    public class OfferTests {
        @Test
        public void shouldReturn403WhenNotAuthenticated() throws Exception {
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
