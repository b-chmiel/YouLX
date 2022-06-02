package com.youlx.api.rest.tag;

import com.youlx.api.Routes;
import com.youlx.domain.tag.TagSearchService;
import com.youlx.domain.tag.TagService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.Tag.TAG)
class TagController {
    private final TagService service;
    private final TagSearchService findService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns tags sorted by reference count descending.")
    })
    ResponseEntity<List<TagDto>> getAll() {
        final var result = service.getAll().stream().map(TagDto::new).toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<Void> create(Principal user, @Valid @RequestBody TagDto tag) throws URISyntaxException {
        service.create(tag.toDomain());
        return ResponseEntity.created(new URI(Routes.Tag.TAG)).build();
    }

    @GetMapping("/search")
    ResponseEntity<List<TagDto>> search(@Valid @RequestParam String query) {
        final var result = findService.search(query).stream().map(TagDto::new).toList();
        return ResponseEntity.ok(result);
    }
}
