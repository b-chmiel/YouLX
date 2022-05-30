package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.api.rest.tag.TagDto;
import com.youlx.domain.tag.TagService;
import com.youlx.domain.user.UserId;
import com.youlx.domain.utils.exception.ApiConflictException;
import com.youlx.domain.utils.exception.ApiException;
import com.youlx.domain.utils.exception.ApiNotFoundException;
import com.youlx.domain.utils.exception.ApiUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(Routes.Offer.OFFERS)
@RequiredArgsConstructor
class OfferTagController {
    private final TagService service;

    @PostMapping("{offerId}/tag")
    ResponseEntity<?> assign(Principal user, @PathVariable String offerId, @Valid @RequestBody TagDto tag) {
        try {
            service.assignToOffer(new UserId(user), offerId, tag.toDomain());
        } catch (ApiUnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (ApiNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ApiConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
