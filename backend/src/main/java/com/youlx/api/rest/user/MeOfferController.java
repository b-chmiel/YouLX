package com.youlx.api.rest.user;

import com.youlx.api.Routes;
import com.youlx.api.rest.offer.OfferDto;
import com.youlx.api.rest.offer.OfferModelAssembler;
import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.find.OfferFindService;
import com.youlx.domain.offer.find.OfferStatusQuery;
import com.youlx.domain.offer.find.OfferTagQuery;
import com.youlx.domain.user.UserId;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.User.ME)
class MeOfferController {
    private final PagedResourcesAssembler<Offer> resourcesAssembler;
    private final OfferModelAssembler modelAssembler;
    private final OfferFindService service;

    @GetMapping("/offers")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<PagedModel<EntityModel<OfferDto>>> offers(
            @ParameterObject @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable,
            Principal user,
            @Parameter(description = "Statuses delimited with ';' symbol. Pass 'ALL' for no filtering.") @RequestParam(required = false, defaultValue = "OPEN") String statuses,
            @Parameter(description = "Tags delimited with ';' symbol. Empty list means no filtering.") @RequestParam(required = false, defaultValue = "") String tags
    ) {
        final var result = service.findBy(pageable, new UserId(user), new OfferStatusQuery(statuses), new OfferTagQuery(tags));
        return ResponseEntity.ok(resourcesAssembler.toModel(result, modelAssembler));
    }
}
