package com.api.rest.offer.conversation;

import com.api.rest.offer.OfferDto;
import com.domain.conversation.Conversation;
import com.domain.offer.Offer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class OfferConversationDto {
    private String id;
    private String posterId;
    private String browserId;

    private OfferDto offer;

    OfferConversationDto(Conversation conversation, Optional<Offer> offer) {
        this.id = conversation.id();
        this.posterId = conversation.poster().getUsername();
        this.browserId = conversation.browser().getUsername();
        this.offer = offer.map(OfferDto::new).orElse(null);
    }
}
