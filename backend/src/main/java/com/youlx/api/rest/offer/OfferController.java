package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.offer.*;
import com.youlx.domain.user.UserService;
import com.youlx.domain.user.UserId;
import com.youlx.domain.utils.exception.ApiException;
import com.youlx.domain.utils.exception.ApiNotFoundException;
import com.youlx.domain.utils.exception.ApiUnauthorizedException;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

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
    ResponseEntity<?> create(Principal user, @Valid @RequestBody OfferCreateDto offer) throws Exception {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        final var userData = userService.findById(user.getName());
        if (userData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        final var created = service.create(offer.toDomain(userData.get()));
        final var uri = new URI(Routes.Offer.OFFERS + '/' + created.getId());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("{id}")
    ResponseEntity<?> get(Principal user, @Valid @PathVariable String id) {
        if (!offerStateCheckService.isVisible(new UserId(user), id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return offerFindService
                .findById(id)
                .map(o -> ResponseEntity.ok(modelAssembler.toModel(o)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("{id}/close")
    ResponseEntity<?> close(Principal user, @Valid @PathVariable String id) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            final var result = service.close(id, new OfferClose(OfferCloseReason.MANUAL), new UserId(user));
            return ResponseEntity.ok(modelAssembler.toModel(result));
        } catch (ApiNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
    ResponseEntity<?> modify(Principal user, @Valid @PathVariable String id, @Valid @RequestBody OfferCreateDto offer) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            service.modify(id, new OfferModify(offer.getName(), offer.getDescription(), offer.getPrice()), new UserId(user));
        } catch (ApiNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ApiUnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/publish")
    ResponseEntity<?> publish(Principal user, @Valid @PathVariable String id) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            service.publish(new UserId(user), id);
            return ResponseEntity.ok().build();
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    ResponseEntity<?> search(Principal user, @Valid @RequestParam String query) {
        final var result = offerFindService.search(new UserId(user), query).stream().map(OfferDto::new);
        return ResponseEntity.ok(result);
    }
}
