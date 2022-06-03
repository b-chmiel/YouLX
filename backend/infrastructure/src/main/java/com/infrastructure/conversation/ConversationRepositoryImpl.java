package com.infrastructure.conversation;

import com.domain.conversation.Conversation;
import com.domain.conversation.ConversationRepository;
import com.domain.conversation.Message;
import com.domain.user.UserId;
import com.domain.utils.exception.ApiCustomException;
import com.domain.utils.exception.ApiException;
import com.domain.utils.exception.ApiNotFoundException;
import com.domain.utils.hashId.HashId;
import com.infrastructure.offer.JpaOfferRepository;
import com.infrastructure.user.JpaUserRepository;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ConversationRepositoryImpl implements ConversationRepository {
    private final JpaMessageRepository messageRepository;
    private final JpaConversationRepository conversationRepository;
    private final JpaOfferRepository offerRepository;
    private final JpaUserRepository userRepository;
    private final HashId hashId;

    @Override
    public void send(Message message, Conversation conversation, UserId userId) throws ApiException {
        try {
            final var user = userRepository.getById(userId.getUsername());
            final var messageTuple = new MessageTuple(message, user);
            final var savedMessage = messageRepository.save(messageTuple);

            final var conversationTuple = conversationRepository.getById(hashId.decode(conversation.getId()));
            conversationTuple.getMessages().add(savedMessage);
            conversationRepository.save(conversationTuple);
        } catch (EntityNotFoundException e) {
            throw new ApiNotFoundException("Conversation not found: " + e.getMessage());
        }
    }

    @Override
    public Stream<Conversation> findByUser(UserId userId) {
        final var user = userRepository.getById(userId.getUsername());
        final var browser = conversationRepository.findAllByBrowser(user).stream().map(c -> c.toDomain(hashId));
        final var poster = conversationRepository.findAllByPoster(user).stream().map(c -> c.toDomain(hashId));

        return Stream.concat(browser, poster);
    }

    @Override
    public boolean exists(String offerId, UserId userId) throws ApiException {
        try {
            final var offer = offerRepository.getById(hashId.decode(offerId));
            final var user = userRepository.getById(userId.getUsername());
            return conversationRepository.existsByOfferAndBrowser(offer, user) || conversationRepository.existsByOfferAndPoster(offer, user);
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    @Override
    public Conversation createConversation(String offerId, UserId userId) {
        final var offer = offerRepository.getById(hashId.decode(offerId));
        final var user = userRepository.getById(userId.getUsername());
        final var conversation = new ConversationTuple(offer, user);

        return conversationRepository.save(conversation).toDomain(hashId);
    }

    @Override
    public Conversation findConversationBy(String offerId, UserId userId) throws ApiException {
        final var user = userRepository.getById(userId.getUsername());
        final var offer = offerRepository.getById(hashId.decode(offerId));
        final var posterConversation = conversationRepository.findAllByPosterAndOffer(user, offer).stream();
        final var browserConversation = conversationRepository.findAllByBrowserAndOffer(user, offer).stream();
        final var conversation = Stream.concat(posterConversation, browserConversation).distinct().toList();

        if (conversation.isEmpty()) {
            throw new ApiNotFoundException("Conversation not found.");
        } else if (conversation.size() != 1) {
            throw new ApiCustomException("Multiple conversations per offer per user is not allowed.");
        }

        return conversation.get(0).toDomain(hashId);
    }
}
