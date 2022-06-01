package com.youlx.api.rest.user;

import com.youlx.api.Routes;
import com.youlx.domain.offer.OfferFindService;
import com.youlx.domain.user.UserId;
import com.youlx.testUtils.MvcHelpers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MeOfferControllerTests {
    @Autowired
    private MvcHelpers helpers;

    @MockBean
    private OfferFindService service;

    @Nested
    class GetOffers {
        @Test
        void notAuthenticated() throws Exception {
            helpers.getRequest(Routes.User.ME + "/offers").andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser("user")
        void get() throws Exception {
            when(service.findBy(any(), any(UserId.class), anyString(), anyString())).thenReturn(Page.empty());
            helpers.getRequest(Routes.User.ME + "/offers?size=0&page=0").andExpect(status().isOk());
        }
    }
}
