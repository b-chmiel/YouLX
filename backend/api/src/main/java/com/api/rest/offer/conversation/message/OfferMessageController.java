package com.api.rest.offer.conversation.message;

import com.api.Routes;
import com.domain.conversation.ConversationId;
import com.domain.conversation.ConversationService;
import com.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(Routes.Conversation.CONVERSATIONS)
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
class OfferMessageController {
    private final ConversationService service;

    @PostMapping("/{conversationId}/messages")
    ResponseEntity<Void> send(Principal user, @Valid @RequestBody OfferMessageCreateDto message, @Valid @PathVariable String conversationId) {
        final var userId = new UserId(user);
        service.send(userId, message.toDomain(userId), new ConversationId(conversationId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{conversationId}/messages")
    ResponseEntity<List<OfferMessageDto>> get(Principal user, @Valid @PathVariable String conversationId) {
        final var result = service.find(new UserId(user), new ConversationId(conversationId)).map(OfferMessageDto::new).toList();
        return ResponseEntity.ok(result);
    }
}
