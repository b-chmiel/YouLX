package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.offer.*;
import com.youlx.domain.user.UserService;
import com.youlx.domain.utils.ApiException;
import com.youlx.domain.utils.ApiNotFoundException;
import com.youlx.domain.utils.ApiUnauthorizedException;
import lombok.RequiredArgsConstructor;
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
    private final OfferService service;
    private final UserService userService;

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
    ResponseEntity<?> get(@Valid @PathVariable String id) {
        final var result = service.findById(id);
        return result.isPresent() ?
                ResponseEntity.ok(modelAssembler.toModel(result.get())) :
                ResponseEntity.notFound().build();
    }

    @PostMapping("{id}/close")
    ResponseEntity<?> close(Principal user, @Valid @PathVariable String id) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        final var result = service.close(id, new OfferClose(OfferCloseReason.MANUAL), user.getName());
        return result.isPresent() ?
                ResponseEntity.ok(modelAssembler.toModel(result.get())) :
                ResponseEntity.notFound().build();
    }

    @GetMapping
    PagedModel<EntityModel<OfferDto>> getAllOpen(
            @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return resourcesAssembler.toModel(service.findOpen(pageable), modelAssembler);
    }

    @PutMapping("{id}")
    ResponseEntity<?> modify(Principal user, @Valid @PathVariable String id, @Valid @RequestBody OfferCreateDto offer) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            service.modify(id, new OfferModify(offer.getName(), offer.getDescription()), user.getName());
        } catch (ApiNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ApiUnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
