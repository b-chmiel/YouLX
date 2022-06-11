package com.api.rest.tag;

import com.api.Routes;
import com.domain.tag.TagService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.Tag.TAG)
class TagController {
    private final TagService service;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns tags sorted by reference count descending.")
    })
    ResponseEntity<List<TagDto>> getAll(@Valid @RequestParam(required = false, defaultValue = "") String query) {
        final var result = service.getAll(query).stream().map(TagDto::new).toList();
        return ResponseEntity.ok(result);
    }
}
