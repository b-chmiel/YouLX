package com.youlx.infrastructure;

import com.youlx.domain.photo.PhotoRepository;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.offer.OfferRepositoryImpl;
import com.youlx.infrastructure.photo.PhotoRepositoryImpl;
import com.youlx.infrastructure.tag.TagRepositoryImpl;
import com.youlx.infrastructure.user.UserRepositoryImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {OfferRepositoryImpl.class, UserRepositoryImpl.class, PhotoRepositoryImpl.class, TagRepositoryImpl.class}, considerNestedRepositories = true)
@EntityScan(basePackageClasses = {OfferRepositoryImpl.class, UserRepositoryImpl.class, PhotoRepositoryImpl.class, TagRepositoryImpl.class})
public class JpaConfig {
    @Bean
    OfferRepositoryImpl offerRepository(OfferRepositoryImpl.Repo repo, HashId hashId, OfferRepositoryImpl.UserRepo userRepo, PhotoRepository photoRepository) {
        return new OfferRepositoryImpl(repo, hashId, userRepo, photoRepository);
    }

    @Bean
    UserRepositoryImpl userRepository(UserRepositoryImpl.Repo repo) {
        return new UserRepositoryImpl(repo);
    }

    @Bean
    PhotoRepositoryImpl photoRepository(PhotoRepositoryImpl.Repo repo, PhotoRepositoryImpl.OfferRepo offerRepo, HashId hashId) {
        return new PhotoRepositoryImpl(repo, offerRepo, hashId);
    }

    @Bean
    TagRepositoryImpl tagRepository(TagRepositoryImpl.Repo repo) {
        return new TagRepositoryImpl(repo);
    }
}
