package com.api.rest.conversation;

import com.domain.conversation.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class MessageDto {
    private String content;

    private String time;
    private String userId;

    MessageDto(Message message) {
        this.content = message.getContent();
        this.time = message.getTime().toString();
        this.userId = message.getUserId().getUsername();
    }
}
