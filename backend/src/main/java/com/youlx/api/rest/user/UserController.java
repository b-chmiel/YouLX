package com.youlx.api.rest.user;

import com.youlx.api.Routes;
import com.youlx.api.rest.offer.OfferDto;
import com.youlx.domain.offer.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {
    private final OfferService service;

    @GetMapping(Routes.User.ME)
    public ResponseEntity<?> me(Principal user) {
        // TODO connect to userService
        return ResponseEntity.ok(new UserDto("", "", "", user.getName()));
    }

    @GetMapping(Routes.User.USER + "/{id}/offers")
    public ResponseEntity<?> offers(Principal user, @Valid @PathVariable String id) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        final var result = service.findByUserId(id);

        return result.isPresent() ?
                ResponseEntity.ok(result.get().stream().map(OfferDto::new).toList()) :
                ResponseEntity.notFound().build();
    }
}
