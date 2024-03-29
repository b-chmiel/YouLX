package com.api.rest.user;

import com.api.Routes;
import com.api.rest.offer.OfferDto;
import com.api.rest.offer.OfferModelAssembler;
import com.domain.offer.Offer;
import com.domain.offer.find.OfferFindService;
import com.domain.offer.find.OfferSearchQuery;
import com.domain.offer.find.OfferStatusQuery;
import com.domain.offer.find.OfferTagQuery;
import com.domain.user.UserId;
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

import javax.validation.Valid;
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
            @Parameter(description = "Tags delimited with ';' symbol. Empty list means no filtering.") @RequestParam(required = false, defaultValue = "") String tags,
            @Valid @RequestParam(required = false, defaultValue = "") String query
    ) {
        final var result = service.findBy(pageable, new UserId(user), new OfferStatusQuery(statuses), new OfferTagQuery(tags), new OfferSearchQuery(query));
        return ResponseEntity.ok(resourcesAssembler.toModel(result, modelAssembler));
    }
}
