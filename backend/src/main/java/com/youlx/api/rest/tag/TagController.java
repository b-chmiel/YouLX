package com.youlx.api.rest.tag;

import com.youlx.api.Routes;
import com.youlx.domain.tag.TagFindService;
import com.youlx.domain.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.Tag.TAG)
class TagController {
    private final TagService service;
    private final TagFindService findService;

    @GetMapping
    ResponseEntity<Stream<TagDto>> getAll() {
        final var result = service.getAll().stream().map(TagDto::new);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<?> create(Principal user, @Valid @RequestBody TagDto tag) throws URISyntaxException {
        service.create(tag.toDomain());
        return ResponseEntity.created(new URI(Routes.Tag.TAG)).build();
    }

    @GetMapping("/search")
    ResponseEntity<Stream<TagDto>> search(@Valid @RequestParam String query) {
        final var result = findService.search(query).stream().map(TagDto::new);
        return ResponseEntity.ok(result);
    }
}
