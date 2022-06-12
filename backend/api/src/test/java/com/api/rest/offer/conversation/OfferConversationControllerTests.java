package com.api.rest.offer.conversation;

import com.api.MvcHelpers;
import com.api.Routes;
import com.domain.conversation.Conversation;
import com.domain.conversation.ConversationService;
import com.domain.conversation.Message;
import com.domain.offer.find.OfferFindService;
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
import java.util.Optional;
import java.util.stream.Stream;

import static com.api.Fixtures.offer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "local")
class OfferConversationControllerTests {
    @Autowired
    private MvcHelpers helpers;

    @MockBean
    private ConversationService service;

    @MockBean
    private OfferFindService findService;

    @Nested
    class GetAll {
        @Test
        void unauthorized() throws Exception {
            helpers.getRequest(Routes.Conversation.CONVERSATIONS).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser("user")
        void getAll() throws Exception {
            final var current = new UserId("user");
            final var other = new UserId("other");
            final var id = "id";
            final var messages = Stream.of(new Message("content", LocalDateTime.now(), current));
            final var conversation = new Conversation(id, current, other, offer.getId(), messages);

            when(service.findAll(current)).thenReturn(Stream.of(conversation));
            when(findService.findById(offer.getId())).thenReturn(Optional.of(offer));

            final var result = helpers.getRequest(Routes.Conversation.CONVERSATIONS).andExpect(status().isOk());

            assertEquals(id, MvcHelpers.attributeFromResult("[0].id", result));
            assertEquals(current.getUsername(), MvcHelpers.attributeFromResult("[0].posterId", result));
            assertEquals(other.getUsername(), MvcHelpers.attributeFromResult("[0].browserId", result));

            assertEquals(offer.getCreationDate(), LocalDateTime.parse(MvcHelpers.attributeFromResult("[0].offer.creationDate", result)));
            assertEquals(offer.getPublishedDate(), LocalDateTime.parse(MvcHelpers.attributeFromResult("[0].offer.publishedDate", result)));
            assertEquals(offer.getClosedDate(), LocalDateTime.parse(MvcHelpers.attributeFromResult("[0].offer.closedDate", result)));
        }
    }
}
