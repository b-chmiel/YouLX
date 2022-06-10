package com.domain.conversation;

import com.domain.user.UserId;

import java.util.stream.Stream;

public record Conversation(String id, UserId poster, UserId browser, String offerId, Stream<Message> messages) {
    public boolean hasMember(UserId userId) {
        return userId.equals(poster) || userId.equals(browser);
    }
}
