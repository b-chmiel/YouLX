package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.find.OfferFindService;
import com.youlx.domain.offer.find.OfferTagQuery;
import com.youlx.domain.user.UserService;
import com.youlx.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.User.USER)
class OfferUserController {
    private final PagedResourcesAssembler<Offer> resourcesAssembler;
    private final OfferModelAssembler modelAssembler;
    private final UserService userService;
    private final OfferFindService offerFindService;

    @GetMapping("/{username}/offers")
    ResponseEntity<PagedModel<EntityModel<OfferDto>>> offers(
            @ParameterObject @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable,
            @Valid @PathVariable String username,
            @RequestParam(required = false, defaultValue = "") String tags
    ) {
        if (!userService.exists(username)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                resourcesAssembler.toModel(
                        offerFindService.findOpen(pageable, new UserId(username), new OfferTagQuery(tags))
                        , modelAssembler
                )
        );
    }
}
