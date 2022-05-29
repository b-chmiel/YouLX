package com.youlx.domain.offer;

import com.youlx.api.Routes;
import com.youlx.domain.photo.Photo;
import com.youlx.domain.tag.Tag;
import com.youlx.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
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

    public Offer(String name, String description, User user, BigDecimal price) {
        this(name, description, user, List.of(), price);
    }

    public Offer(String name, String description, User user, List<Photo> photos, BigDecimal price) {
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
        this.tags = Set.of();
    }

    public List<String> photosUrls() {
        return this.photos.stream().map(p -> Routes.Offer.OFFERS + "/" + this.id + "/photos/" + p.getId()).toList();
    }

    public void close(OfferCloseReason reason) {
        this.status = OfferStatus.CLOSED;
        this.closeReason = Optional.of(reason);
    }
}
