package com.youlx.api.rest.offer;

import com.sun.security.auth.UserPrincipal;
import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferCloseReason;
import com.youlx.domain.offer.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
class OfferModelAssembler implements RepresentationModelAssembler<Offer, EntityModel<OfferDto>> {
    private final OfferService offerService;

    @Override
    public EntityModel<OfferDto> toModel(Offer entity) {
        final var dto = new OfferDto(entity);
        return EntityModel.of(dto, createLinks(entity));
    }

    private List<Link> createLinks(Offer entity) {
        var links = new ArrayList<Link>();

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof final UserDetails user) {
            if (offerService.isClosable(user, entity)) {
                final var principal = new UserPrincipal(user.getUsername());
                links.add(linkTo(methodOn(OfferController.class).close(principal, entity.getId(), new OfferCloseDto(OfferCloseReason.MANUAL))).withRel("close"));
            }
        }

        links.addAll(
                List.of(
                        linkTo(methodOn(OfferController.class).get(entity.getId())).withSelfRel(),
                        linkTo(methodOn(OfferController.class).getAllOpen(Pageable.unpaged())).withRel("allOpenOffers")
                )
        );

        return links;
    }
}
