package com.domain.conversation;

import com.domain.user.UserId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Message {
    private final String content;
    private final LocalDateTime time;
    private final UserId userId;

    public Message(String content, UserId userId) {
        this.content = content;
        this.time = LocalDateTime.now();
        this.userId = userId;
    }
}
