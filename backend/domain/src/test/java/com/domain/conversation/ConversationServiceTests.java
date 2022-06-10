package com.domain.conversation;

import com.domain.offer.stateCheck.OfferStateCheckService;
import com.domain.user.UserId;
import com.domain.user.UserService;
import com.domain.utils.exception.ApiConflictException;
import com.domain.utils.exception.ApiNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ConversationServiceTests {
    private final OfferStateCheckService offerStateCheckService = mock(OfferStateCheckService.class);
    private final ConversationRepository repository = mock(ConversationRepository.class);
    private final UserService userService = mock(UserService.class);
    private final ConversationService service = new ConversationServiceImpl(userService, offerStateCheckService, repository);

    @Nested
    class SendTests {
        @Test
        void userDoesNotExist() {
            final var user = new UserId("asdf");
            assertThrows(ApiNotFoundException.class, () -> service.send(user, null, null));
        }

        @Test
        void conversationDoesNotExist() {
            final var user = new UserId("asdf");
            final var conversation = new Conversation("", user, user, "", Stream.of());

            when(userService.exists(user.getUsername())).thenReturn(true);
            when(repository.findById(new ConversationId(conversation))).thenReturn(Optional.empty());

            assertThrows(ApiNotFoundException.class, () -> service.send(user, null, new ConversationId(conversation)));
        }

        @Test
        void userIsNotMemberOfConversation() {
            final var user = new UserId("user");
            final var conversation = new Conversation("", new UserId("a"), new UserId("b"), "", Stream.of());

            when(userService.exists(user.getUsername())).thenReturn(true);
            when(repository.findById(new ConversationId(conversation))).thenReturn(Optional.of(conversation));

            assertThrows(ApiNotFoundException.class, () -> service.send(user, null, new ConversationId(conversation)));
        }

        @Test
        void offerNotVisibleToUser() {
            final var user = new UserId("asdf");
            final var offerId = "offerId";
            final var conversation = new Conversation("", user, user, "", Stream.of());

            when(userService.exists(user.getUsername())).thenReturn(true);
            when(repository.findById(new ConversationId(conversation))).thenReturn(Optional.of(conversation));
            when(offerStateCheckService.isVisible(user, offerId)).thenReturn(false);

            assertThrows(ApiNotFoundException.class, () -> service.send(user, null, new ConversationId(conversation)));
        }
    }

    @Nested
    class FindAllTests {
        @Test
        void userNotFound() {
            final var user = new UserId("asdf");

            when(userService.exists(user.getUsername())).thenReturn(false);

            assertThrows(ApiNotFoundException.class, () -> service.findAll(user));
        }

        @Test
        void findAll() {
            final var user = new UserId("asdf");
            final var offerId = "a";
            final var messages = List.of(new Message("mes1", user), new Message("mes2", user));
            final var conversation = new Conversation("id", new UserId("other1"), new UserId("other2"), offerId, messages.stream());
            final var conversations = Stream.of(conversation);

            when(userService.exists(user.getUsername())).thenReturn(true);
            when(repository.findByUser(user)).thenReturn(conversations);

            assertEquals(conversations, service.findAll(user));
        }
    }

    @Nested
    class CreateConversationTests {
        @Test
        void offerNotVisible() {
            final var user = new UserId("asdf");
            final var offerId = "a";

            when(offerStateCheckService.isVisible(user, offerId)).thenReturn(false);

            assertThrows(ApiNotFoundException.class, () -> service.createConversation(user, offerId));
        }

        @Test
        void conversationExists() {
            final var user = new UserId("asdf");
            final var offerId = "a";

            when(offerStateCheckService.isVisible(user, offerId)).thenReturn(true);
            when(repository.exists(offerId, user)).thenReturn(true);

            assertThrows(ApiConflictException.class, () -> service.createConversation(user, offerId));
        }

        @Test
        void createConversation() {
            final var user = new UserId("asdf");
            final var offerId = "a";

            when(offerStateCheckService.isVisible(user, offerId)).thenReturn(true);
            when(repository.exists(offerId, user)).thenReturn(false);

            service.createConversation(user, offerId);

            verify(repository).createConversation(offerId, user);
        }
    }
}
