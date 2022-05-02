package com.youlx.infrastructure.offer;

import com.youlx.api.rest.offer.OfferController;
import com.youlx.domain.offer.Offer;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OfferModelAssembler implements RepresentationModelAssembler<OfferTuple, EntityModel<Offer>> {
    @Override
    public EntityModel<Offer> toModel(OfferTuple entity) {
        return EntityModel.of(entity.toDomain(), linkTo(methodOn(OfferController.class).get(entity.getId())).withSelfRel());
    }
}
