package com.api.rest.offer.conversation;

import com.api.Routes;
import com.domain.conversation.ConversationService;
import com.domain.offer.find.OfferFindService;
import com.domain.user.UserId;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping(Routes.Conversation.CONVERSATIONS)
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
class OfferConversationController {
    private final ConversationService service;
    private final OfferFindService findService;

    @GetMapping
    ResponseEntity<List<OfferConversationDto>> getAll(Principal user) {
        final var result = service
                .findAll(new UserId(user))
                .map(
                        conversation -> new OfferConversationDto(conversation, findService.findById(conversation.offerId()))
                ).toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{offerId}")
    @Operation(summary = "Create conversation for offer.")
    ResponseEntity<OfferConversationDto> create(Principal user, @Valid @PathVariable String offerId) throws URISyntaxException {
        final var conversation = service.createConversation(new UserId(user), offerId);
        final var result = new OfferConversationDto(conversation, findService.findById(conversation.offerId()));

        return ResponseEntity.created(new URI(Routes.Conversation.CONVERSATIONS)).body(result);
    }
}
