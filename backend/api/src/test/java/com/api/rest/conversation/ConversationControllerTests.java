package com.api.rest.conversation;

import com.api.MvcHelpers;
import com.api.Routes;
import com.domain.conversation.ConversationService;
import com.domain.user.UserId;
import com.domain.utils.time.Time;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ConversationControllerTests {
    @Autowired
    private MvcHelpers helpers;

    @MockBean
    private ConversationService service;

    @MockBean
    private Time time;

    @Nested
    class SendTests {
        @Test
        void unauthorized() throws Exception {
            final var msg = new MessageCreateDto("message");
            helpers.postRequest(msg, Routes.Message.MESSAGE + "/asdf").andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser("usr")
        void send() throws Exception {
            final var offerId = "id";
            final var msg = new MessageCreateDto("message");
            final var now = LocalDateTime.now();
            when(time.now()).thenReturn(now);
            when(time.now()).thenReturn(now);

            helpers.postRequest(msg, Routes.Message.MESSAGE + '/' + offerId).andExpect(status().isOk());

            verify(service, times(1)).send(new UserId("usr"), msg.toDomain(time), offerId);
        }
    }
}
