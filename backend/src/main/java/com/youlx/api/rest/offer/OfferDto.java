package com.youlx.api.rest.offer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.youlx.domain.offer.Offer;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "offers", itemRelation = "offer")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferDto extends RepresentationModel<OfferDto> {
    private Long id;
    private String name;
    private String description;
    private String status;

    public OfferDto(Offer offer) {
        this.id = offer.getId();
        this.name = offer.getName();
        this.description = offer.getDescription();
        this.status = offer.getStatus().name();
    }
}
