package com.api.rest.offer.conversation.message;

import com.api.rest.DateSerializer;
import com.domain.conversation.Message;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class OfferMessageDto {
    private String content;
    @JsonSerialize(using = DateSerializer.class)
    private LocalDateTime time;
    private String userId;

    OfferMessageDto(Message message) {
        this.content = message.getContent();
        this.time = message.getTime();
        this.userId = message.getUserId().getUsername();
    }
}
