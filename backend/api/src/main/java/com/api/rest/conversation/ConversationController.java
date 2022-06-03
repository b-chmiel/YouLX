package com.api.rest.conversation;

import com.api.Routes;
import com.domain.conversation.ConversationService;
import com.domain.user.UserId;
import com.domain.utils.time.Time;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(Routes.Message.MESSAGE)
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
class ConversationController {
    private final ConversationService service;

    @PostMapping("{offerId}")
    ResponseEntity<Void> send(Principal user, @Valid @RequestBody MessageCreateDto message, @Valid @PathVariable String offerId) {
        final var userId = new UserId(user);
        service.send(userId, message.toDomain(userId), offerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{offerId}")
    ResponseEntity<ConversationDto> get(Principal user, @Valid @PathVariable String offerId) {
        return ResponseEntity.ok(new ConversationDto(service.find(new UserId(user), offerId)));
    }

    @GetMapping
    ResponseEntity<List<ConversationDto>> getAll(Principal user) {
        final var result = service.findAll(new UserId(user)).map(ConversationDto::new).toList();
        return ResponseEntity.ok(result);
    }
}
