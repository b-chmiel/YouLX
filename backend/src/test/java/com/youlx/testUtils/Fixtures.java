package com.youlx.testUtils;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferStatus;
import com.youlx.domain.photo.Photo;
import com.youlx.domain.tag.Tag;
import com.youlx.domain.user.User;
import com.youlx.domain.utils.uuid.UuidImpl;
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
            photo = new Photo(new UuidImpl(), new MockMultipartFile("index.jpg", new ClassPathResource("fixtures/index.jpg").getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final static User user = new User(List.of(), "", "", "", "", "a", "");
    public final static Offer offer = new Offer("1", "offer1", "description1", OfferStatus.OPEN, LocalDateTime.now(), Optional.empty(), user, List.of(), BigDecimal.TEN, LocalDateTime.now(), LocalDateTime.now(), Set.of());
    public final static Tag tag = new Tag("cars");
}
