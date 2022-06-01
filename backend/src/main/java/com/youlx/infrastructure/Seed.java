package com.youlx.infrastructure;

import com.youlx.api.config.SecurityRoles;
import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferCloseReason;
import com.youlx.domain.offer.OfferRepository;
import com.youlx.domain.photo.Photo;
import com.youlx.domain.user.User;
import com.youlx.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Seed implements ApplicationRunner {
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String mockDescription = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final var user1 = new User(List.of(new SimpleGrantedAuthority(SecurityRoles.USER.name())), "user1", "user1", "user1", passwordEncoder.encode("user1"), "user1", "+48555555555");
        final var user2 = new User(List.of(new SimpleGrantedAuthority(SecurityRoles.USER.name())), "user2", "user2", "user2", passwordEncoder.encode("user2"), "user2", "+48555555555");
        final var admin = new User(List.of(new SimpleGrantedAuthority(SecurityRoles.USER.name())), "admin", "admin", "admin", passwordEncoder.encode("admin"), "admin", "+48555555555");
        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.create(admin);

        final var photo = new Photo(new ClassPathResource("fixtures/index.jpg").getInputStream().readAllBytes());
        offerRepository.create(new Offer("Offer0", mockDescription, admin, List.of(), BigDecimal.valueOf(1.123)));
        final var o = new Offer("Offer1", mockDescription, admin, List.of(photo), BigDecimal.valueOf(2.123));
        offerRepository.publish(offerRepository.create(o).getId());

        final var o2 = new Offer("Offer2", mockDescription, admin, List.of(), BigDecimal.valueOf(2.123));
        o2.close(OfferCloseReason.EXPIRED);
        offerRepository.create(o2);

        final var offer = new Offer("Offer3", mockDescription, user1, List.of(), BigDecimal.valueOf(2.123));
        offer.close(OfferCloseReason.EXPIRED);
        offerRepository.create(offer);
    }
}
