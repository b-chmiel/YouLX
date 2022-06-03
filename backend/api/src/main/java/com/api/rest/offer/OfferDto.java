package com.api.rest.offer;

import com.api.rest.user.UserDto;
import com.domain.offer.Offer;
import com.domain.offer.modify.OfferCloseReason;
import com.domain.tag.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "offers", itemRelation = "offer")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferDto extends RepresentationModel<OfferDto> {
    private String id;
    private String name;
    private String description;
    private String status;
    private LocalDateTime creationDate;
    private OfferCloseReason closeReason;
    private UserDto user;
    private String coverUrl;
    private List<String> imageUrls;
    private BigDecimal price;
    private LocalDateTime publishedDate;
    private LocalDateTime closedDate;
    private Set<Tag> tags;

    OfferDto(Offer offer) {
        this.id = offer.getId();
        this.name = offer.getName();
        this.description = offer.getDescription();
        this.status = offer.getStatus().name();
        this.creationDate = offer.getCreationDate();
        this.closeReason = offer.getCloseReason().orElse(null);
        this.user = new UserDto(offer.getUser());

        final var urls = offer.photosUrls();
        if (!urls.isEmpty()) {
            this.coverUrl = urls.get(0);
        }

        this.imageUrls = urls;
        this.price = offer.getPrice();
        this.publishedDate = offer.getPublishedDate();
        this.closedDate = offer.getClosedDate();
        this.tags = offer.getTags();
    }
}
