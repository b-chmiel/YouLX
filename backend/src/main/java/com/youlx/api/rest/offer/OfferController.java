package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.offer.*;
import com.youlx.domain.user.UserId;
import com.youlx.domain.user.UserService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.Offer.OFFERS)
class OfferController {
    private final PagedResourcesAssembler<Offer> resourcesAssembler;
    private final OfferModelAssembler modelAssembler;
    private final OfferModifyService service;
    private final OfferFindService offerFindService;
    private final UserService userService;
    private final OfferStateCheckService offerStateCheckService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<Void> create(Principal user, @Valid @RequestBody OfferCreateDto offer) throws Exception {
        final var userData = userService.findById(user.getName());
        if (userData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        final var created = service.create(offer.toDomain(userData.get()));
        final var uri = new URI(Routes.Offer.OFFERS + '/' + created.getId());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("{id}")
    ResponseEntity<EntityModel<OfferDto>> get(Principal user, @Valid @PathVariable String id) {
        if (!offerStateCheckService.isVisible(new UserId(user), id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return offerFindService
                .findById(id)
                .map(o -> ResponseEntity.ok(modelAssembler.toModel(o)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("{id}/close")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<EntityModel<OfferDto>> close(Principal user, @Valid @PathVariable String id) {
        final var result = service.close(id, new OfferClose(OfferCloseReason.MANUAL), new UserId(user));
        return ResponseEntity.ok(modelAssembler.toModel(result));
    }

    @GetMapping
    PagedModel<EntityModel<OfferDto>> getAllOpen(
            @ParameterObject @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String tags
    ) {
        final var result = offerFindService.findOpen(pageable, new UserId(), tags);
        return resourcesAssembler.toModel(result, modelAssembler);
    }

    @PutMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<Void> modify(Principal user, @Valid @PathVariable String id, @Valid @RequestBody OfferCreateDto offer) {
        service.modify(id, new OfferModify(offer.getName(), offer.getDescription(), offer.getPrice()), new UserId(user));
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/publish")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<Void> publish(Principal user, @Valid @PathVariable String id) {
        service.publish(new UserId(user), id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    ResponseEntity<List<OfferDto>> search(Principal user, @Valid @RequestParam String query) {
        final var result = offerFindService
                .search(new UserId(user), query)
                .stream()
                .map(OfferDto::new)
                .toList();
        return ResponseEntity.ok(result);
    }
}
