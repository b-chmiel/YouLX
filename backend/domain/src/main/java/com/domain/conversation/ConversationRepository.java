package com.domain.conversation;

import com.domain.user.UserId;
import com.domain.utils.exception.ApiException;

import java.util.stream.Stream;

public interface ConversationRepository {
    void send(Message message, Conversation conversation, UserId userId);

    Stream<Conversation> findByUser(UserId userId);

    boolean exists(String offerId, UserId user);

    Conversation createConversation(String offerId, UserId userId);

    Conversation findConversationBy(String offerId, UserId userId) throws ApiException;
}
