package com.domain.conversation;

import com.domain.user.UserId;
import com.domain.utils.exception.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.stream.Stream;

public interface ConversationService {
    void send(UserId userId, Message message, ConversationId conversationId) throws ApiException;

    Stream<Message> find(UserId userId, ConversationId conversationId);

    Stream<Conversation> findAll(UserId userId);

    Conversation createConversation(UserId userId, String offerId) throws ApiException;
}
