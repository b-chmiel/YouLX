package com.youlx.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me")
@SecurityRequirement(name = "X-API-KEY")
public class UserController {
    @GetMapping
    public ResponseEntity<?> me(Principal user) {
        return ResponseEntity.ok(new UserDto(user.getName()));
    }
}
