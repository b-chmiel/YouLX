package com.youlx.api.rest.offer.photo;

import com.youlx.api.Routes;
import com.youlx.domain.offer.OfferService;
import com.youlx.domain.photo.Photo;
import com.youlx.testUtils.Fixtures;
import com.youlx.testUtils.MvcHelpers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OfferPhotoControllerTests {
    @Autowired
    private MvcHelpers helpers;
    @MockBean
    private OfferService service;

    @Nested
    class PostPhotoTests {
        @Test
        void unauthorizedOnUnauthorized() throws Exception {
            final var id = "a";
            helpers.postRequest(null, Routes.Offer.OFFERS + "/" + id + "/photos").andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        void post() throws Exception {
            final var id = "a";
            final var url = Routes.Offer.OFFERS + "/" + id + "/photos";

            final var file = new MockMultipartFile("file", "index.jpg", MediaType.IMAGE_JPEG_VALUE, Fixtures.photo.getData());

            helpers.postFile(file, url).andExpect(status().isOk());
            verify(service, times(1)).savePhoto(id, new Photo(file));
        }
    }
}
