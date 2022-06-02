package com.youlx.domain.offer.find;

import com.youlx.domain.offer.OfferStatus;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class OfferStatusQuery {
    private final List<OfferStatus> statuses;

    public OfferStatusQuery(String status) {
        this.statuses = Arrays.stream(status.split(";"))
                .filter(t -> !t.isBlank())
                .map(OfferStatus::fromString)
                .filter(t -> !t.equals(OfferStatus.ERROR))
                .toList();
    }
}
