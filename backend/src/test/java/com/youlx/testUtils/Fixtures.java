package com.youlx.testUtils;

import com.youlx.domain.photo.Photo;
import com.youlx.domain.user.User;
import com.youlx.domain.utils.uuid.UuidImpl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;

public class Fixtures {
    public static final Photo photo;

    static {
        try {
            photo = new Photo(new UuidImpl(), new MockMultipartFile("index.jpg", new ClassPathResource("fixtures/index.jpg").getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final static User user = new User(List.of(), "", "", "", "", "a");
}
