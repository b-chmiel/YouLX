package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferFindService;
import com.youlx.domain.user.UserShallow;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.User.ME)
class OfferMeController {
    private final PagedResourcesAssembler<Offer> resourcesAssembler;
    private final OfferModelAssembler modelAssembler;
    private final OfferFindService service;

    @GetMapping("/offers")
    ResponseEntity<PagedModel<EntityModel<OfferDto>>> offers(
            @ParameterObject @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable,
            Principal user,
            @Parameter(description = "Statuses delimited with ';' symbol. Pass 'ALL' for no filtering.") @RequestParam(required = false, defaultValue = "OPEN") String statuses,
            @Parameter(description = "Tags delimited with ';' symbol. Empty list means no filtering.") @RequestParam(required = false, defaultValue = "") String tags
    ) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        final var username = user.getName();
        return ResponseEntity.ok(
                resourcesAssembler.toModel(service.findBy(pageable, new UserShallow(username), statuses, tags)
                        , modelAssembler
                )
        );
    }
}
