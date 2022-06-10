package com.api.rest.offer.conversation.message;

import com.domain.conversation.Message;
import com.domain.user.UserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class OfferMessageCreateDto {
    @NotNull
    private String content;

    public Message toDomain(UserId userId) {
        return new Message(content, userId);
    }
}
