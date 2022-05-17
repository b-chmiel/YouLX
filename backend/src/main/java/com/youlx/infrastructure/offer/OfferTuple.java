package com.youlx.infrastructure.offer;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferCloseReason;
import com.youlx.domain.offer.OfferStatus;
import com.youlx.domain.user.User;
import com.youlx.infrastructure.user.UserTuple;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

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
    @ManyToOne(fetch = FetchType.EAGER)
    private UserTuple user;
    private LocalDateTime creationDate;
    @Nullable
    private OfferCloseReason closeReason;

    public OfferTuple(Offer offer, UserTuple user) {
        name = offer.getName();
        description = offer.getDescription();
        status = offer.getStatus();
        this.user = user;
        creationDate = offer.getCreationDate();
        closeReason = offer.getCloseReason().orElse(null);
    }

    public Offer toDomain(String hashedId) {
        return new Offer(hashedId, name, description, status, creationDate, Optional.ofNullable(closeReason), user.toDomain());
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
