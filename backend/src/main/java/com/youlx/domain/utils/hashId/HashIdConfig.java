package com.youlx.domain.utils.hashId;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HashIdConfig {
    @Value("${hashids.salt}")
    private String salt;

    private static final int minHashLength = 10;

    @Bean
    public HashId hashId() throws ApiHashIdException {
        if (salt.isEmpty()) {
            throw new ApiHashIdException("Salt cannot be empty");
        }
        final var hashids = new Hashids(salt, minHashLength);
        return new HashIdImpl(hashids);
    }
}
