package com.api.rest.offer;

import com.api.MvcHelpers;
import com.api.Routes;
import com.domain.user.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@PropertySource("classpath:application-local.yml")
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
            when(userService.exists(username)).thenReturn(false);
            helpers.getRequest(Routes.User.USER + "/" + username + "/offers").andExpect(status().isNotFound());
        }

        @Test
        void showOffersWhenUserExists() throws Exception {
            final var username = "a";
            when(userService.exists(username)).thenReturn(true);
            helpers.getRequest(Routes.User.USER + "/" + username + "/offers").andExpect(status().isOk());
        }
    }
}
