package com.youlx.infrastructure.offer;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "LX_OFFER")
public class OfferTuple {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Lob
    private String description;
    private OfferStatus status;
    private String userId;
    private LocalDateTime creationDate;

    public OfferTuple(Offer offer) {
        name = offer.getName();
        description = offer.getDescription();
        status = offer.getStatus();
        userId = offer.getUserId();
        creationDate = offer.getCreationDate();
    }

    public Offer toDomain(String hashedId) {
        return new Offer(hashedId, name, description, status, userId, creationDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfferTuple that = (OfferTuple) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
