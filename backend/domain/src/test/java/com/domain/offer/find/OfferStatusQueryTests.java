package com.domain.offer.find;

import com.domain.offer.OfferStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OfferStatusQueryTests {
    @Test
    void create() {
        final var query = new OfferStatusQuery("OPEN;CLOSED;ERROR;UNRECOGNIZED;DRAFT");
        assertEquals(List.of(OfferStatus.OPEN, OfferStatus.CLOSED, OfferStatus.DRAFT), query.getStatuses());
    }
}
