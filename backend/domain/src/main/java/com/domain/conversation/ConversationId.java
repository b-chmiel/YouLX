package com.domain.conversation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ConversationId {
    private final String id;

    public ConversationId(Conversation conversation) {
        this.id = conversation == null ? null : conversation.id();
    }
}
