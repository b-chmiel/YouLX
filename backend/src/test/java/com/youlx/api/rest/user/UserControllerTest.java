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
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MvcHelpers helpers;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferRepository offerRepository;

    private final String mockUserLogin = "test-user";

    @Nested
    class MeGetTests {
        @Test
        void profileReturns403IfUserIsNotAuthenticated() throws Exception {
            helpers.getRequest(Routes.User.ME).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(mockUserLogin)
        void profileReturnsNotFoundForNonExistingUser() throws Exception {
            offerRepository.clear();
            userRepository.clear();

            final var result = helpers.getRequest(Routes.User.ME);
            result.andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser("e")
        void profileReturnsInfoOfUser() throws Exception {
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
    class MePostTests {
        @Test
        void returns403IfUserIsNotAuthenticated() throws Exception {
            helpers.postRequest(null, Routes.User.ME).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        void returns400OnMalformedRequest() throws Exception {
            helpers.postRequest(null, Routes.User.ME).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        }

        @Test
        @WithMockUser("doesnotexist")
        void returns304WhenUserDoesNotExist() throws Exception {
            helpers.postRequest(new UserEditDto("", "", ""), Routes.User.ME).andExpect(status().is(HttpStatus.NOT_MODIFIED.value()));
        }

        @Test
        @WithMockUser("e")
        void editsUserData() throws Exception {
            final var user = new User(List.of(), "a", "b", "c", "d", "e");
            userRepository.create(user);

            final var userEdit = new UserEditDto("aa", "bb", "cc");
            helpers.postRequest(userEdit, Routes.User.ME).andExpect(status().isOk());

            final var edited = userRepository.findByUsername(user.getUsername());
            assertEquals(userEdit.getEmail(), edited.get().getEmail());
            assertEquals(userEdit.getFirstName(), edited.get().getFirstName());
            assertEquals(userEdit.getLastName(), edited.get().getLastName());
        }
    }

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
