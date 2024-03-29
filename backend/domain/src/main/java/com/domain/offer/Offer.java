package com.domain.offer;

import com.domain.offer.modify.OfferCloseReason;
import com.domain.photo.Photo;
import com.domain.tag.Tag;
import com.domain.user.User;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Offer {
    private final String id;
    private final String name;
    private final String description;
    @Setter
    private OfferStatus status;
    private final LocalDateTime creationDate;

    @Setter
    private Optional<OfferCloseReason> closeReason;
    private final User user;
    private final List<Photo> photos;
    private final BigDecimal price;
    private LocalDateTime publishedDate;
    private LocalDateTime closedDate;
    private final Set<Tag> tags;

    public Offer(String name, String description, User user, BigDecimal price, Set<Tag> tags) {
        this(name, description, user, List.of(), price, tags);
    }

    public Offer(String name, String description, User user, List<Photo> photos, BigDecimal price, Set<Tag> tags) {
        this.id = null;
        this.name = name;
        this.description = description;
        this.status = OfferStatus.DRAFT;
        this.creationDate = LocalDateTime.now();
        this.closeReason = Optional.empty();
        this.user = user;
        this.photos = photos;
        this.price = price;
        this.publishedDate = null;
        this.closedDate = null;
        this.tags = tags;
    }

    public List<String> photosUrls() {
        return this.photos.stream().map(p -> "/api/offers/" + this.id + "/photos/" + p.getId()).toList();
    }

    public void close(OfferCloseReason reason) {
        this.status = OfferStatus.CLOSED;
        this.closeReason = Optional.of(reason);
    }
}
