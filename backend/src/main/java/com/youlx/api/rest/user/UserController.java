package com.youlx.api.rest.user;

import com.youlx.api.Routes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {
    @GetMapping(Routes.User.ME)
    public ResponseEntity<?> me(Principal user) {
        // TODO connect to userService
        return ResponseEntity.ok(new UserDto(0L, "", "", "", user.getName()));
    }
}
