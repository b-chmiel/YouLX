package com.youlx.infrastructure;

import com.youlx.infrastructure.offer.OfferRepositoryImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {OfferRepositoryImpl.class}, considerNestedRepositories = true)
@EntityScan(basePackageClasses = {OfferRepositoryImpl.class})
public class JpaConfig {
    @Bean
    OfferRepositoryImpl orderRepository(OfferRepositoryImpl.Repo repo) {
        return new OfferRepositoryImpl(repo);
    }
}
