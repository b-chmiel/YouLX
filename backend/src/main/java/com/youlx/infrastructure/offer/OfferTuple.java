package com.youlx.infrastructure.offer;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferCloseReason;
import com.youlx.domain.offer.OfferStatus;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.photo.PhotoTuple;
import com.youlx.infrastructure.tag.TagTuple;
import com.youlx.infrastructure.user.UserTuple;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "LX_OFFER")
@Indexed
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OfferTuple {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Field(termVector = TermVector.YES)
    private String name;
    @Lob
    @Type(type = "org.hibernate.type.MaterializedClobType")
    @Field(termVector = TermVector.YES)
    private String description;
    private OfferStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserTuple user;
    private LocalDateTime creationDate;
    @Nullable
    private OfferCloseReason closeReason;
    private BigDecimal price;
    private LocalDateTime publishedDate;
    private LocalDateTime closedDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = PhotoTuple.class)
    private List<PhotoTuple> photos;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = TagTuple.class)
    private Set<TagTuple> tags;

    public OfferTuple(Offer offer, UserTuple user) {
        this.name = offer.getName();
        this.description = offer.getDescription();
        this.status = offer.getStatus();
        this.user = user;
        this.creationDate = offer.getCreationDate();
        this.closeReason = offer.getCloseReason().orElse(null);
        this.photos = new ArrayList<>();
        this.price = offer.getPrice();
        this.publishedDate = offer.getPublishedDate();
        this.closedDate = offer.getClosedDate();
        this.tags = new HashSet<>();
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
                photos.stream().map(p -> p.toDomain(hasher)).toList(),
                price,
                publishedDate,
                closedDate,
                tags.stream().map(TagTuple::toDomain).collect(Collectors.toSet())
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
