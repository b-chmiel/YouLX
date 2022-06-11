package com.domain.conversation;

import com.domain.offer.stateCheck.OfferStateCheckService;
import com.domain.user.UserId;
import com.domain.user.UserService;
import com.domain.utils.exception.ApiConflictException;
import com.domain.utils.exception.ApiException;
import com.domain.utils.exception.ApiNotFoundException;
import com.domain.utils.exception.ApiUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final UserService userService;
    private final OfferStateCheckService offerStateCheckService;
    private final ConversationRepository repository;

    @Override
    public void send(UserId userId, Message message, ConversationId conversationId) throws ApiException {
        final var conversation = repository.findById(conversationId);

        checkIfConversationIsVisible(userId, conversation);

        repository.send(message, conversationId, userId);
    }

    @Override
    public Stream<Message> find(UserId userId, ConversationId conversationId) {
        final var conversation = repository.findById(conversationId);
        checkIfConversationIsVisible(userId, conversation);

        return conversation.get().messages();
    }

    private void checkIfConversationIsVisible(UserId userId, Optional<Conversation> conversation) {
        if (!userService.exists(userId.getUsername())) {
            throw new ApiNotFoundException("User not found.");
        }

        if (conversation.isEmpty()) {
            throw new ApiNotFoundException("Conversation not found.");
        }

        if (!conversation.get().hasMember(userId)) {
            throw new ApiUnauthorizedException("User is not member of conversation.");
        }

        final var offerId = conversation.get().offerId();
        if (!offerStateCheckService.isVisible(userId, offerId)) {
            throw new ApiNotFoundException("Offer not found.");
        }
    }

    @Override
    public Stream<Conversation> findAll(UserId userId) {
        if (!userService.exists(userId.getUsername())) {
            throw new ApiNotFoundException("User not found.");
        }

        return repository.findByUser(userId);
    }

    @Override
    public Conversation createConversation(UserId userId, String offerId) throws ApiException {
        if (!offerStateCheckService.isVisible(userId, offerId)) {
            throw new ApiNotFoundException("Offer not found.");
        }
        if (repository.exists(offerId, userId)) {
            throw new ApiConflictException("Conversation already exists.");
        }

        return repository.createConversation(offerId, userId);
    }
}