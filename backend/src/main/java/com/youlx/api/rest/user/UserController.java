package com.youlx.api.rest.user;

import com.youlx.api.Routes;
import com.youlx.api.rest.offer.OfferDto;
import com.youlx.domain.offer.OfferService;
import com.youlx.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return userService.findById(user.getName())
                .map(u -> ResponseEntity.ok(new UserDto(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(Routes.User.ME)
    ResponseEntity<?> me(Principal user, @Valid @RequestBody UserEditDto userData) {
        return userService.edit(user.getName(), userData.toDomain())
                .map(u -> ResponseEntity.ok(new UserDto(u)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
    }

    @GetMapping(Routes.User.USER + "/{id}/offers")
    ResponseEntity<?> offers(Principal user, @Valid @PathVariable String id) {
        final var result = offerService.findByUserId(id);

        return result.isPresent() ?
                ResponseEntity.ok(result.get().stream().map(OfferDto::new).toList()) :
                ResponseEntity.notFound().build();
    }
}
