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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        offerRepository.create(new Offer(null, "Offer0", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", OfferStatus.OPEN, "admin", LocalDateTime.now(), Optional.empty()));
        offerRepository.create(new Offer(null, "Offer1", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", OfferStatus.CLOSED, "user1", LocalDateTime.now(), Optional.of(OfferCloseReason.EXPIRED)));

        userRepository.create(new User(List.of(new SimpleGrantedAuthority(SecurityRoles.ADMIN.name())), "admin", "admin", "admin", passwordEncoder.encode("admin"), "admin"));
        userRepository.create(new User(List.of(new SimpleGrantedAuthority(SecurityRoles.USER.name())), "user1", "user1", "user1", passwordEncoder.encode("user1"), "user1"));
        userRepository.create(new User(List.of(new SimpleGrantedAuthority(SecurityRoles.USER.name())), "user2", "user2", "user2", passwordEncoder.encode("user2"), "user2"));
    }
}
