package com.youlx.domain.offer;

import com.youlx.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class Offer {
    private final String id;
    private final String name;
    private final String description;
    private OfferStatus status;
    private final LocalDateTime creationDate;
    private Optional<OfferCloseReason> closeReason;
    private final User user;

    public Offer(String name, String description, User user) {
        this.id = null;
        this.name = name;
        this.description = description;
        this.status = OfferStatus.OPEN;
        this.creationDate = LocalDateTime.now();
        this.closeReason = Optional.empty();
        this.user = user;
    }

    public void close(OfferCloseReason reason) {
        this.status = OfferStatus.CLOSED;
        this.closeReason = Optional.of(reason);
    }
}
