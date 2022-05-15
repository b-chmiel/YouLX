package com.youlx.api.rest.offer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferCloseReason;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Data
@Builder
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

    public OfferDto(Offer offer) {
        this.id = offer.getId();
        this.name = offer.getName();
        this.description = offer.getDescription();
        this.status = offer.getStatus().name();
        this.creationDate = offer.getCreationDate();
        this.closeReason = offer.getCloseReason().orElse(null);
    }
}
