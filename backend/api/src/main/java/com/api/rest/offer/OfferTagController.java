package com.api.rest.offer;

import com.api.Routes;
import com.api.rest.tag.TagDto;
import com.domain.tag.TagService;
import com.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(Routes.Offer.OFFERS)
@RequiredArgsConstructor
class OfferTagController {
    private final TagService service;

    @PostMapping("{offerId}/tag")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<Void> assign(Principal user, @PathVariable String offerId, @Valid @RequestBody TagDto tag) {
        service.assignToOffer(new UserId(user), offerId, tag.toDomain());
        return ResponseEntity.ok().build();
    }
}
