package com.youlx.api.rest.tag;

import com.youlx.api.Routes;
import com.youlx.domain.tag.TagService;
import com.youlx.domain.utils.exception.ApiConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.Tag.TAG)
class TagController {
    private final TagService service;

    @GetMapping
    ResponseEntity<?> getAll() {
        final var result = service.getAll().stream().map(TagDto::new);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    ResponseEntity<?> create(Principal user, @Valid @RequestBody TagDto tag) throws URISyntaxException {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            service.create(tag.toDomain());
        } catch (ApiConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        return ResponseEntity.created(new URI(Routes.Tag.TAG)).build();
    }
}
