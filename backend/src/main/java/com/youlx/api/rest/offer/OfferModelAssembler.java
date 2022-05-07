package com.youlx.api.rest.offer;

import com.youlx.domain.offer.Offer;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OfferModelAssembler implements RepresentationModelAssembler<Offer, EntityModel<OfferDto>> {
    @Override
    public EntityModel<OfferDto> toModel(Offer entity) {
        final var dto = new OfferDto(entity);
        return EntityModel.of(dto, linkTo(methodOn(OfferController.class).get(entity.getId())).withSelfRel());
    }
}
