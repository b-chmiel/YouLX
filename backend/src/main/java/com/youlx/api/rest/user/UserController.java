package com.youlx.api.rest.user;

import com.youlx.api.Routes;
import com.youlx.api.rest.offer.OfferDto;
import com.youlx.domain.offer.OfferService;
import com.youlx.domain.user.UserService;
import lombok.RequiredArgsConstructor;
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
class UserController {
    private final OfferService offerService;
    private final UserService userService;

    @GetMapping(Routes.User.ME)
    ResponseEntity<?> me(Principal user) {
        final var result = userService.findById(user.getName());
        return result.isPresent() ? ResponseEntity.ok(new UserDto(result.get())) : ResponseEntity.notFound().build();
    }

    @GetMapping(Routes.User.USER + "/{id}/offers")
    ResponseEntity<?> offers(Principal user, @Valid @PathVariable String id) {
        final var result = offerService.findByUserId(id);

        return result.isPresent() ?
                ResponseEntity.ok(result.get().stream().map(OfferDto::new).toList()) :
                ResponseEntity.notFound().build();
    }
}
