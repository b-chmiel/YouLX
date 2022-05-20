package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferService;
import com.youlx.domain.user.UserService;
import lombok.RequiredArgsConstructor;
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
    private final OfferService offerService;

    @GetMapping("/{username}/offers")
    ResponseEntity<PagedModel<EntityModel<OfferDto>>> offers(
            @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable,
            @Valid @PathVariable String username,
            @RequestParam(required = false, defaultValue = "OPEN") String status
    ) {
        if (userService.findById(username).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                resourcesAssembler.toModel(
                        offerService.findBy(pageable, username, status)
                        , modelAssembler
                )
        );
    }
}
