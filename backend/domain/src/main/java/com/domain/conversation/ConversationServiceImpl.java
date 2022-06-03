package com.domain.conversation;

import com.domain.offer.find.OfferFindService;
import com.domain.offer.stateCheck.OfferStateCheckService;
import com.domain.user.UserId;
import com.domain.user.UserService;
import com.domain.utils.exception.ApiException;
import com.domain.utils.exception.ApiNotFoundException;
import com.domain.utils.exception.ApiUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final UserService userService;
    private final OfferFindService offerFindService;
    private final OfferStateCheckService offerStateCheckService;
    private final ConversationRepository repository;

    @Override
    public void send(UserId userId, Message message, String offerId) throws ApiException {
        checkIfUserAndOfferExist(userId, offerId);

        final Conversation conversation;
        if (!repository.exists(offerId, userId)) {
            conversation = repository.createConversation(offerId, userId);
        } else {
            conversation = repository.findConversationBy(offerId, userId);
        }

        if (!conversation.hasMember(userId)) {
            throw new ApiUnauthorizedException("User is not member of conversation.");
        }

        repository.send(message, conversation, userId);
    }

    @Override
    public Conversation find(UserId userId, String offerId) throws ApiException {
        checkIfUserAndOfferExist(userId, offerId);

        return repository.findConversationBy(offerId, userId);
    }

    @Override
    public Stream<Conversation> findAll(UserId userId) {
        if (!userService.exists(userId.getUsername())) {
            throw new ApiNotFoundException("User not found.");
        }

        return repository.findByUser(userId);
    }

    private void checkIfUserAndOfferExist(UserId userId, String offerId) throws ApiException {
        if (!userService.exists(userId.getUsername())) {
            throw new ApiNotFoundException("User not found.");
        }

        if (!offerFindService.exists(offerId) || !offerStateCheckService.isVisible(userId, offerId)) {
            throw new ApiNotFoundException("Offer not found.");
        }
    }
}