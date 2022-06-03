package com.domain.conversation;

import com.domain.user.UserId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public class Conversation {
    private final String id;
    private final UserId poster;
    private final UserId browser;
    private final String offerId;
    private final Stream<Message> messages;

    public boolean hasMember(UserId userId) {
        return userId.equals(poster) || userId.equals(browser);
    }
}
