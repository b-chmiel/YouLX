package com.infrastructure.conversation;

import com.domain.conversation.Conversation;
import com.domain.conversation.ConversationId;
import com.domain.conversation.ConversationRepository;
import com.domain.conversation.Message;
import com.domain.user.UserId;
import com.domain.utils.exception.ApiNotFoundException;
import com.domain.utils.hashId.HashId;
import com.domain.utils.hashId.HashIdImpl;
import com.infrastructure.Fixtures;
import com.infrastructure.offer.JpaOfferRepository;
import com.infrastructure.offer.OfferTuple;
import com.infrastructure.user.JpaUserRepository;
import com.infrastructure.user.UserTuple;
import org.hashids.Hashids;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.transaction.Transactional;
import java.util.List;

import static com.infrastructure.Fixtures.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ContextConfiguration(
        classes = {HashIdImpl.class, Hashids.class, ConversationRepositoryImpl.class},
        loader = AnnotationConfigContextLoader.class
)
@EnableJpaRepositories("com.infrastructure")
@EntityScan("com.infrastructure")
@Transactional
class ConversationRepositoryTests {

    @Autowired
    private HashId hashId;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaConversationRepository conversationRepository;

    @Autowired
    private JpaOfferRepository offerRepository;

    @Autowired
    private ConversationRepository repository;

    @Nested
    class SendTests {
        @Test
        void userNotFound() {
            final var user = new UserId("doesNotExist");
            final var message = new Message("content", user);
            final var offerId = hashId.encode(1L);
            final var messages = List.of(new Message("mes1", user), new Message("mes2", user));
            final var conversation = new Conversation(hashId.encode(2L), user, new UserId("other2"), offerId, messages.stream());

            assertThrows(ApiNotFoundException.class, () -> repository.send(message, new ConversationId(conversation), user));
        }

        @Test
        void conversationNotFound() {
            final var userId = new UserId(user);
            final var message = new Message("content", userId);
            final var offerId = hashId.encode(1L);
            final var messages = List.of(new Message("mes1", userId), new Message("mes2", userId));
            final var conversation = new Conversation(hashId.encode(2L), userId, new UserId("other2"), offerId, messages.stream());

            userRepository.save(new UserTuple(user));

            assertThrows(ApiNotFoundException.class, () -> repository.send(message, new ConversationId(conversation), userId));
        }

        @Test
        void send() {
            final var userId = new UserId(user);
            final var message = new Message("content", userId);
            final var offerId = hashId.encode(1L);

            final var savedUser = userRepository.save(new UserTuple(user));
            final var offer = offerRepository.save(new OfferTuple(Fixtures.offer, savedUser));
            final var conversation = conversationRepository.save(new ConversationTuple(offer, savedUser)).toDomain(hashId);

            assertEquals(List.of(), repository.findById(new ConversationId(conversation)).get().messages().toList());

            repository.send(message, new ConversationId(conversation), userId);

            assertEquals(List.of(message), repository.findById(new ConversationId(conversation)).get().messages().toList());
        }
    }
}
