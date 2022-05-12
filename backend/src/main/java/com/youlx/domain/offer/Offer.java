package com.youlx.domain.offer;

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
    private final String userId;
    private final LocalDateTime creationDate;
    private Optional<OfferCloseReason> closeReason;

    public Offer(String name, String description, String userId) {
        this.id = null;
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.status = OfferStatus.OPEN;
        this.creationDate = LocalDateTime.now();
        this.closeReason = Optional.empty();
    }

    public void close(OfferCloseReason reason) {
        this.status = OfferStatus.CLOSED;
        this.closeReason = Optional.of(reason);
    }
}
