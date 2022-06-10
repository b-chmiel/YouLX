package com.domain.conversation;

import com.domain.user.UserId;

import java.util.Optional;
import java.util.stream.Stream;

public interface ConversationRepository {
    void send(Message message, ConversationId conversation, UserId userId);

    Stream<Conversation> findByUser(UserId userId);

    boolean exists(String offerId, UserId user);

    Conversation createConversation(String offerId, UserId userId);

    Optional<Conversation> findById(ConversationId conversationId);
}
