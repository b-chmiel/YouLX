package com.youlx.infrastructure;

import com.youlx.domain.user.UserRepository;
import com.youlx.domain.utils.HashId;
import com.youlx.infrastructure.offer.OfferRepositoryImpl;
import com.youlx.infrastructure.user.UserRepositoryImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {OfferRepositoryImpl.class, UserRepositoryImpl.class}, considerNestedRepositories = true)
@EntityScan(basePackageClasses = {OfferRepositoryImpl.class, UserRepositoryImpl.class})
public class JpaConfig {
    @Bean
    OfferRepositoryImpl offerRepository(OfferRepositoryImpl.Repo repo, HashId hashId, OfferRepositoryImpl.UserRepo userRepo) {
        return new OfferRepositoryImpl(repo, hashId, userRepo);
    }

    @Bean
    UserRepositoryImpl userRepository(UserRepositoryImpl.Repo repo) {
        return new UserRepositoryImpl(repo);
    }
}
