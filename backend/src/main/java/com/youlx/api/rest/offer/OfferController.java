package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferService;
import com.youlx.domain.offer.OfferStatus;
import com.youlx.domain.utils.HashId;
import com.youlx.infrastructure.offer.OfferPagedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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

    @PostMapping
    public ResponseEntity<?> create(Principal user, @Valid @RequestBody OfferCreateDto offer) throws URISyntaxException {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        final var created = service.create(offer.toDomain(user.getName()));
        final var uri = new URI(Routes.Offer.OFFERS + '/' + created.getId());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@Valid @PathVariable String id) {
        final var result = service.findById(id);
        return result.isPresent() ?
                ResponseEntity.ok(result) :
                ResponseEntity.notFound().build();
    }

    @GetMapping
    public PagedModel<EntityModel<OfferDto>> getAllOpen(Pageable pageable) {
        final var offersTuple = repository.findAllByStatus(pageable, OfferStatus.OPEN);
        final var offers = offersTuple.map(
                t -> t.toDomain(hashId.encode(t.getId()))
        );
        return resourcesAssembler.toModel(offers, modelAssembler);
    }
}
