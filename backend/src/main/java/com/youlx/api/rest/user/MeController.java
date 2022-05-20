package com.youlx.api.rest.user;

import com.youlx.api.Routes;
import com.youlx.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(Routes.User.ME)
@RequiredArgsConstructor
class MeController {
    private final UserService userService;

    @GetMapping
    ResponseEntity<?> me(Principal user) {
        return userService.findById(user.getName())
                .map(u -> ResponseEntity.ok(new UserDto(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    ResponseEntity<?> me(Principal user, @Valid @RequestBody UserEditDto userData) {
        return userService.edit(user.getName(), userData.toDomain())
                .map(u -> ResponseEntity.ok(new UserDto(u)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
    }
}
