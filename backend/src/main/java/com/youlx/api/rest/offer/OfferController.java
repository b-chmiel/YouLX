package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferService;
import com.youlx.domain.offer.OfferStatus;
import com.youlx.domain.user.UserService;
import com.youlx.domain.utils.HashId;
import com.youlx.infrastructure.offer.OfferPagedRepository;
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
public class OfferController {
    private final OfferPagedRepository repository;
    private final PagedResourcesAssembler<Offer> resourcesAssembler;
    private final OfferModelAssembler modelAssembler;
    private final OfferService service;
    private final HashId hashId;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> create(Principal user, @Valid @RequestBody OfferCreateDto offer) throws Exception {
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
    public ResponseEntity<?> get(@Valid @PathVariable String id) {
        final var result = service.findById(id);
        return result.isPresent() ?
                ResponseEntity.ok(new OfferDto(result.get())) :
                ResponseEntity.notFound().build();
    }

    @PostMapping("{id}/close")
    public ResponseEntity<?> close(Principal user, @Valid @PathVariable String id, @Valid @RequestBody OfferCloseDto offerClose) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        final var result = service.close(id, offerClose.toDomain(), user.getName());
        return result.isPresent() ?
                ResponseEntity.ok(new OfferDto(result.get())) :
                ResponseEntity.notFound().build();
    }

    @GetMapping
    public PagedModel<EntityModel<OfferDto>> getAllOpen(@PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        final var offersTuple = repository.findAllByStatus(pageable, OfferStatus.OPEN);
        final var offers = offersTuple.map(
                t -> t.toDomain(hashId.encode(t.getId()))
        );
        return resourcesAssembler.toModel(offers, modelAssembler);
    }
}
