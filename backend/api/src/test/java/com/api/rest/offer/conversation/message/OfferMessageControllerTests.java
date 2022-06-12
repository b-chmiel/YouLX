package com.api.rest.offer.conversation.message;

import com.api.MvcHelpers;
import com.api.Routes;
import com.domain.conversation.ConversationId;
import com.domain.conversation.ConversationService;
import com.domain.conversation.Message;
import com.domain.user.UserId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "local")
class OfferMessageControllerTests {
    @Autowired
    private MvcHelpers helpers;

    @MockBean
    private ConversationService service;

    @Nested
    class SendTests {
        @Test
        void unauthorized() throws Exception {
            helpers.postRequest(new OfferMessageCreateDto("content"), Routes.Conversation.CONVERSATIONS + "/asdf/messages").andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser("user")
        void send() throws Exception {
            final var message = new OfferMessageCreateDto("content");
            final var conversationId = "id";
            final var user = new UserId("user");

            helpers.postRequest(message, Routes.Conversation.CONVERSATIONS + '/' + conversationId + "/messages").andExpect(status().isOk());

            verify(service, times(1)).send(eq(user), any(Message.class), any(ConversationId.class));
        }
    }

    @Nested
    class GetTests {
        @Test
        void unauthorized() throws Exception {
            helpers.getRequest(Routes.Conversation.CONVERSATIONS + "/asdf/messages").andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser("user")
        void send() throws Exception {
            final var user = new UserId("user");
            final var messages = List.of(new Message("mes1", user), new Message("mes2", user));
            final var conversationId = new ConversationId("a");

            when(service.find(any(UserId.class), any(ConversationId.class))).thenReturn(messages.stream());
            final var result = helpers.getRequest(Routes.Conversation.CONVERSATIONS + '/' + conversationId + "/messages").andExpect(status().isOk());

            for (var i = 0; i < messages.size(); ++i) {
                assertEquals(messages.get(i).getContent(), MvcHelpers.attributeFromResult("[" + i + "].content", result));
                assertEquals(messages.get(i).getTime(), LocalDateTime.parse(MvcHelpers.attributeFromResult("[" + i + "].time", result)));
                assertEquals(messages.get(i).getUserId().getUsername(), MvcHelpers.attributeFromResult("[" + i + "].userId", result));
            }
        }
    }
}
