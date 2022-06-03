package com.domain.conversation;

import com.domain.user.UserId;
import com.domain.utils.exception.ApiException;

import java.util.stream.Stream;

public interface ConversationService {
    void send(UserId userId, Message message, String offerId) throws ApiException;

    Conversation find(UserId userId, String offerId);

    Stream<Conversation> findAll(UserId userId);
}
