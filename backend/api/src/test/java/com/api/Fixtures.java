package com.api;

import com.domain.offer.Offer;
import com.domain.offer.OfferStatus;
import com.domain.photo.Photo;
import com.domain.tag.Tag;
import com.domain.user.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Fixtures {
    public static final Photo photo;

    static {
        try {
            photo = new Photo(new MockMultipartFile("index.jpg", new ClassPathResource("fixtures/index.jpg").getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final static User user = new User(List.of(), "", "", "", "", "a", "");
    public final static Offer offer = new Offer("1", "offer1", "description1", OfferStatus.OPEN, LocalDateTime.now(), Optional.empty(), user, List.of(), BigDecimal.TEN, LocalDateTime.now(), LocalDateTime.now(), Set.of());
    public final static Tag tag = new Tag("cars");
}
