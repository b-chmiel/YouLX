package com.api.rest.offer;

import com.domain.offer.Offer;
import com.domain.offer.stateCheck.OfferStateCheckService;
import com.domain.user.UserId;
import com.sun.security.auth.UserPrincipal;
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
public class OfferModelAssembler implements RepresentationModelAssembler<Offer, EntityModel<OfferDto>> {
    private final OfferStateCheckService offerStateCheckService;

    @Override
    public EntityModel<OfferDto> toModel(Offer entity) {
        final var dto = new OfferDto(entity);
        return EntityModel.of(dto, createLinks(entity));
    }

    private List<Link> createLinks(Offer entity) {
        var links = new ArrayList<Link>();

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof final UserDetails user) {
            final var principal = new UserPrincipal(user.getUsername());
            links.add(linkTo(methodOn(OfferController.class).get(principal, entity.getId())).withSelfRel());
            if (offerStateCheckService.isClosable(new UserId(user.getUsername()), entity)) {
                links.add(linkTo(methodOn(OfferController.class).close(principal, entity.getId())).withRel("close"));
            } else if (offerStateCheckService.isPublishable(new UserId(user.getUsername()), entity.getId())) {
                links.add(linkTo(methodOn(OfferController.class).publish(principal, entity.getId())).withRel("publish"));
            }
        }

        links.add(
                linkTo(methodOn(OfferController.class).getAllOpen(Pageable.unpaged(), "", "")).withRel("allOpenOffers")
        );

        return links.stream().map(link -> Link.of(link.getHref().replace("http", "https").replace("youlx-backend", "youlx"), link.getRel())).toList();
    }
}
