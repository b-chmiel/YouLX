package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.user.User;
import com.youlx.domain.user.UserService;
import com.youlx.testUtils.MvcHelpers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OfferUserControllerTests {
    @Autowired
    private MvcHelpers helpers;

    @MockBean
    private UserService userService;

    @Nested
    class OfferTests {
        @Test
        void shouldReturn404OnNonExistingUser() throws Exception {
            final var username = "a";
            when(userService.findById(username)).thenReturn(Optional.empty());
            helpers.getRequest(Routes.User.USER + "/" + username + "/offers").andExpect(status().isNotFound());
        }

        @Test
        void showOffersWhenUserExists() throws Exception {
            final var username = "a";
            when(userService.findById(username)).thenReturn(Optional.of(new User(List.of(), "", "", "", "", "", "")));
            helpers.getRequest(Routes.User.USER + "/" + username + "/offers").andExpect(status().isOk());
        }
    }
}
