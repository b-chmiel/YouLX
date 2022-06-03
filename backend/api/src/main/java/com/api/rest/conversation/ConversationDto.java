package com.api.rest.conversation;

import com.domain.conversation.Conversation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class ConversationDto {
    private String id;
    private String posterId;
    private String browserId;
    private String offerId;
    private List<MessageDto> messages;

    ConversationDto(Conversation conversation) {
        this.id = conversation.getId();
        this.posterId = conversation.getPoster().getUsername();
        this.browserId = conversation.getBrowser().getUsername();
        this.offerId = conversation.getOfferId();
        this.messages = conversation.getMessages().map(MessageDto::new).toList();
    }
}
