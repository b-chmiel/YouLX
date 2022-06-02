package com.youlx.infrastructure;

import com.youlx.domain.photo.PhotoRepository;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.offer.JpaOfferRepository;
import com.youlx.infrastructure.offer.OfferFindRepositoryImpl;
import com.youlx.infrastructure.offer.OfferPagedRepository;
import com.youlx.infrastructure.offer.OfferRepositoryImpl;
import com.youlx.infrastructure.photo.JpaPhotoRepository;
import com.youlx.infrastructure.photo.PhotoRepositoryImpl;
import com.youlx.infrastructure.tag.JpaTagRepository;
import com.youlx.infrastructure.tag.TagRepositoryImpl;
import com.youlx.infrastructure.user.JpaUserRepository;
import com.youlx.infrastructure.user.UserRepositoryImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {OfferRepositoryImpl.class, UserRepositoryImpl.class, PhotoRepositoryImpl.class, TagRepositoryImpl.class, OfferFindRepositoryImpl.class}, considerNestedRepositories = true)
@EntityScan(basePackageClasses = {OfferRepositoryImpl.class, UserRepositoryImpl.class, PhotoRepositoryImpl.class, TagRepositoryImpl.class, OfferFindRepositoryImpl.class})
public class JpaConfig {
    @Bean
    OfferRepositoryImpl offerRepository(HashId hashId, JpaUserRepository userRepository, JpaOfferRepository offerRepository, PhotoRepository photoRepository) {
        return new OfferRepositoryImpl(hashId, userRepository, offerRepository, photoRepository);
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
    OfferFindRepositoryImpl offerFindRepository(HashId hashId, OfferPagedRepository offerPagedRepository, JpaTagRepository tagRepository) {
        return new OfferFindRepositoryImpl(hashId, offerPagedRepository, tagRepository);
    }
}
