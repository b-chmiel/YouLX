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
@RequestMapping(Routes.User.ME)
public class UserController {
    @GetMapping
    public ResponseEntity<?> me(Principal user) {
        return ResponseEntity.ok(new UserDto(0L, "", "", "", user.getName()));
    }
}
