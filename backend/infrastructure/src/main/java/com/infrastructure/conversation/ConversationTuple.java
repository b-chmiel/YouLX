package com.infrastructure.conversation;

import com.domain.conversation.Conversation;
import com.domain.user.UserId;
import com.domain.utils.hashId.HashId;
import com.infrastructure.offer.OfferTuple;
import com.infrastructure.user.UserTuple;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "LX_CONVERSATION")
@Getter
@Setter
@ToString
@NoArgsConstructor
class ConversationTuple {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OfferTuple offer;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserTuple browser;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserTuple poster;

    @OneToMany(fetch = FetchType.LAZY)
    private List<MessageTuple> messages;

    ConversationTuple(OfferTuple offer, UserTuple user) {
        this.offer = offer;
        this.browser = user;
        this.poster = offer.getUser();
        this.messages = new ArrayList<>();
    }

    public Conversation toDomain(HashId hashId) {
        final var conversationId = hashId.encode(id);
        final var posterId = new UserId(poster.getId());
        final var browserId = new UserId(browser.getId());
        final var offerId = hashId.encode(offer.getId());
        final var messagesDomain = messages.stream().map(MessageTuple::toDomain);

        return new Conversation(conversationId, posterId, browserId, offerId, messagesDomain);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationTuple that = (ConversationTuple) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
