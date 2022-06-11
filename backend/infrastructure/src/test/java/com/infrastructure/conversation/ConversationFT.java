package com.infrastructure.conversation;

import com.domain.conversation.ConversationId;
import com.domain.conversation.ConversationService;
import com.domain.conversation.ConversationServiceImpl;
import com.domain.offer.find.OfferFindServiceImpl;
import com.domain.offer.stateCheck.OfferStateCheckServiceImpl;
import com.domain.user.PasswordEncoderConfig;
import com.domain.user.UserId;
import com.domain.user.UserServiceImpl;
import com.domain.utils.exception.ApiNotFoundException;
import com.domain.utils.hashId.HashIdImpl;
import org.hashids.Hashids;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ContextConfiguration(classes = {
        ConversationServiceImpl.class,
        UserServiceImpl.class,
        PasswordEncoderConfig.class,
        OfferStateCheckServiceImpl.class,
        HashIdImpl.class,
        Hashids.class,
        OfferFindServiceImpl.class
})
@EnableJpaRepositories("com.infrastructure")
@EntityScan("com.infrastructure")
@Transactional
class ConversationFT {
    @Autowired
    private ConversationService service;

    @Nested
    class SendTests {
        @Test
        void send() {
            final var user = new UserId("a");
            assertThrows(ApiNotFoundException.class, () -> service.send(user, null, new ConversationId("a")));
        }
    }
}
