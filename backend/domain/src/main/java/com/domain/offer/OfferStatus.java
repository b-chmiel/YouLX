package com.domain.offer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OfferStatus {
    OPEN("OPEN"), CLOSED("CLOSED"), DRAFT("DRAFT"), ERROR("ERROR");

    private final String name;

    public static OfferStatus fromString(String text) {
        for (var status : OfferStatus.values()) {
            if (status.name.equalsIgnoreCase(text)) {
                return status;
            }
        }

        return ERROR;
    }
}
