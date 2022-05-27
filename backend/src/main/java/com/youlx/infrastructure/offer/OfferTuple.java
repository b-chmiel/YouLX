package com.youlx.infrastructure.offer;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferCloseReason;
import com.youlx.domain.offer.OfferStatus;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.photo.PhotoTuple;
import com.youlx.infrastructure.user.UserTuple;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    @Type(type = "org.hibernate.type.MaterializedClobType")
    private String description;
    private OfferStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserTuple user;
    private LocalDateTime creationDate;
    @Nullable
    private OfferCloseReason closeReason;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = PhotoTuple.class)
    private List<PhotoTuple> photos;

    public OfferTuple(Offer offer, UserTuple user) {
        this.name = offer.getName();
        this.description = offer.getDescription();
        this.status = offer.getStatus();
        this.user = user;
        this.creationDate = offer.getCreationDate();
        this.closeReason = offer.getCloseReason().orElse(null);
        this.photos = new ArrayList<>();
    }

    public Offer toDomain(HashId hasher) {
        return new Offer(
                hasher.encode(id),
                name,
                description,
                status,
                creationDate,
                Optional.ofNullable(closeReason),
                user.toDomain(),
                photos.stream().map(PhotoTuple::toDomain).toList()
        );
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
