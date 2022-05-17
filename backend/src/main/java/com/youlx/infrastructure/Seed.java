package com.youlx.infrastructure;

import com.youlx.api.config.SecurityRoles;
import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferCloseReason;
import com.youlx.domain.offer.OfferRepository;
import com.youlx.domain.offer.OfferStatus;
import com.youlx.domain.user.User;
import com.youlx.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Seed implements ApplicationRunner {
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String mockDescription = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final var user1 = new User(List.of(new SimpleGrantedAuthority(SecurityRoles.USER.name())), "user1", "user1", "user1", passwordEncoder.encode("user1"), "user1");
        final var user2 = new User(List.of(new SimpleGrantedAuthority(SecurityRoles.USER.name())), "user2", "user2", "user2", passwordEncoder.encode("user2"), "user2");
        final var admin = new User(List.of(new SimpleGrantedAuthority(SecurityRoles.USER.name())), "admin", "admin", "admin", passwordEncoder.encode("admin"), "admin");
        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.create(admin);

        offerRepository.create(new Offer(null, "Offer0", mockDescription, OfferStatus.OPEN, LocalDateTime.now(), Optional.empty(), admin));
        offerRepository.create(new Offer(null, "Offer1", mockDescription, OfferStatus.CLOSED, LocalDateTime.now(), Optional.of(OfferCloseReason.EXPIRED), user1));
    }
}
