package com.infrastructure.conversation;

import com.domain.conversation.ConversationId;
import com.domain.conversation.ConversationService;
import com.domain.conversation.ConversationServiceImpl;
import com.domain.offer.find.OfferFindServiceImpl;
import com.domain.offer.stateCheck.OfferStateCheckServiceImpl;
import com.domain.user.UserId;
import com.domain.user.UserServiceImpl;
import com.domain.utils.exception.ApiNotFoundException;
import com.domain.utils.hashId.HashIdImpl;
import com.infrastructure.JpaConfig;
import org.hashids.Hashids;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(
        classes = {
                JpaConfig.class,
                HashIdImpl.class,
                Hashids.class,
                ConversationServiceImpl.class,
                UserServiceImpl.class,
                BCryptPasswordEncoder.class,
                OfferFindServiceImpl.class,
                OfferStateCheckServiceImpl.class
        },
        loader = AnnotationConfigContextLoader.class
)
@DataJpaTest
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
