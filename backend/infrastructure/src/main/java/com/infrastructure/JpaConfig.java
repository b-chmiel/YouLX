package com.infrastructure;

import com.domain.photo.PhotoRepository;
import com.domain.tag.TagRepository;
import com.domain.utils.hashId.HashId;
import com.infrastructure.conversation.ConversationRepositoryImpl;
import com.infrastructure.conversation.JpaConversationRepository;
import com.infrastructure.conversation.JpaMessageRepository;
import com.infrastructure.offer.JpaOfferRepository;
import com.infrastructure.offer.OfferPagedRepository;
import com.infrastructure.offer.OfferRepositoryImpl;
import com.infrastructure.photo.JpaPhotoRepository;
import com.infrastructure.photo.PhotoRepositoryImpl;
import com.infrastructure.tag.JpaTagRepository;
import com.infrastructure.tag.TagRepositoryImpl;
import com.infrastructure.user.JpaUserRepository;
import com.infrastructure.user.UserRepositoryImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {OfferRepositoryImpl.class, UserRepositoryImpl.class, PhotoRepositoryImpl.class, TagRepositoryImpl.class, ConversationRepositoryImpl.class}, considerNestedRepositories = true)
@EntityScan(basePackageClasses = {OfferRepositoryImpl.class, UserRepositoryImpl.class, PhotoRepositoryImpl.class, TagRepositoryImpl.class, ConversationRepositoryImpl.class})
public class JpaConfig {
    @Bean
    OfferRepositoryImpl offerRepository(HashId hashId, JpaUserRepository userRepository, JpaOfferRepository offerRepository, PhotoRepository photoRepository, TagRepository tagRepository, OfferPagedRepository offerPagedRepository, JpaTagRepository jpaTagRepository) {
        return new OfferRepositoryImpl(hashId, userRepository, offerRepository, photoRepository, tagRepository, offerPagedRepository, jpaTagRepository);
    }

    @Bean
    UserRepositoryImpl userRepository(JpaUserRepository repo) {
        return new UserRepositoryImpl(repo);
    }

    @Bean
    PhotoRepositoryImpl photoRepository(HashId hashId, JpaPhotoRepository photoRepository, JpaOfferRepository offerRepository) {
        return new PhotoRepositoryImpl(hashId, photoRepository, offerRepository);
    }

    @Bean
    TagRepositoryImpl tagRepository(HashId hashId, JpaTagRepository tagRepository, JpaOfferRepository offerRepository) {
        return new TagRepositoryImpl(hashId, tagRepository, offerRepository);
    }

    @Bean
    ConversationRepositoryImpl messageRepository(JpaMessageRepository messageRepository, JpaConversationRepository conversationRepository, JpaOfferRepository offerRepository, JpaUserRepository userRepository, HashId hashId) {
        return new ConversationRepositoryImpl(messageRepository, conversationRepository, offerRepository, userRepository, hashId);
    }
}
