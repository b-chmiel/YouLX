package com.infrastructure;

import com.domain.offer.Offer;
import com.domain.offer.OfferRepository;
import com.domain.offer.modify.OfferClose;
import com.domain.offer.modify.OfferCloseReason;
import com.domain.photo.Photo;
import com.domain.tag.Tag;
import com.domain.tag.TagRepository;
import com.domain.user.User;
import com.domain.user.UserRepository;
import com.domain.utils.exception.ApiException;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class Seed implements ApplicationRunner {
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Random random = new Random(1L);
    private static final Faker faker = new Faker(random);
    private static final int NUMBER_OF_TAGS = 10;
    private static final int PHOTOS_PER_OFFER = 3;
    private static final int OFFER_COUNT = 1;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        final var users = List.of(userFrom("user1"), userFrom("user2"));
        users.forEach(userRepository::create);

        final var photos = List.of(photoFrom("fixtures/photo1.jpg"), photoFrom("fixtures/photo2.jpg"), photoFrom("fixtures/photo3.jpg"), photoFrom("fixtures/photo4.jpg"), photoFrom("fixtures/photo5.jpg"), photoFrom("fixtures/photo6.jpg"), photoFrom("fixtures/photo7.jpg"));

        final var tags = tagsFrom();
        tags.forEach(tagRepository::create);

        for (final var user : users) {
            IntStream.range(0, OFFER_COUNT).forEach(i -> {
                createDraftOffer(user, photos, tags);
                createOpenOffer(user, photos, tags);
                createClosedOffer(user, photos, tags);
            });
        }
    }

    private void createOpenOffer(User user, List<Photo> photos, List<Tag> tags) {
        final var open = offerRepository.create(offerFrom(user, photos));
        offerRepository.publish(open.getId());
        assignTagsToOffer(tags, open.getId());
    }

    private void createDraftOffer(User user, List<Photo> photos, List<Tag> tags) {
        final var draft = offerRepository.create(offerFrom(user, photos));
        assignTagsToOffer(tags, draft.getId());
    }

    private void createClosedOffer(User user, List<Photo> photos, List<Tag> tags) {
        final var closed = offerRepository.create(offerFrom(user, photos));
        offerRepository.publish(closed.getId());
        offerRepository.close(closed.getId(), new OfferClose(OfferCloseReason.MANUAL));
        assignTagsToOffer(tags, closed.getId());
    }

    private void assignTagsToOffer(List<Tag> allTags, String offerId) {
        try {
            tagRepository.assignToOffer(offerId, allTags.get(faker.random().nextInt(allTags.size())));
            tagRepository.assignToOffer(offerId, allTags.get(faker.random().nextInt(allTags.size())));
        } catch (ApiException ignored) {
            // Imma not if this nextInts' randomness out.
        }
    }

    private List<Tag> tagsFrom() {
        return IntStream.range(0, NUMBER_OF_TAGS).mapToObj(i -> faker.lorem().word()).distinct().map(Tag::new).toList();
    }

    private User userFrom(String username) {
        return new User(
                List.of(new SimpleGrantedAuthority("USER")),
                faker.name().firstName(),
                faker.name().lastName(),
                username + "@mail.com",
                passwordEncoder.encode(username),
                username,
                "+48555555555"
        );
    }

    private Photo photoFrom(String path) throws IOException {
        return new Photo(new ClassPathResource(path).getInputStream().readAllBytes());
    }

    private Offer offerFrom(User user, List<Photo> photos) {
        final var name = faker.commerce().productName();
        final var description = faker.lorem().paragraph(80);
        final var selectedPhotos = IntStream.range(0, PHOTOS_PER_OFFER).mapToObj(i -> faker.random().nextInt(photos.size())).map(photos::get).toList();
        final var price = BigDecimal.valueOf(faker.number().randomDouble(2, 1, 200));

        return new Offer(name, description, user, selectedPhotos, price);
    }
}
