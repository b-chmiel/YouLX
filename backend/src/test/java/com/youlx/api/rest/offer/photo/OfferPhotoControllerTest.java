package com.youlx.api.rest.offer.photo;

import com.youlx.api.Routes;
import com.youlx.domain.photo.ApiImageException;
import com.youlx.domain.photo.PhotoService;
import com.youlx.domain.utils.ApiNotFoundException;
import com.youlx.domain.utils.uuid.Uuid;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OfferPhotoControllerTest {
    @Autowired
    private MvcHelpers helpers;
    @MockBean
    private PhotoService service;
    @MockBean
    private Uuid uuid;

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

            when(uuid.generate()).thenReturn(Fixtures.photo.getId());
            final var file = new MockMultipartFile("file", "index.jpg", MediaType.IMAGE_JPEG_VALUE, Fixtures.photo.getData());

            helpers.postFile(file, url).andExpect(status().isOk());
            verify(service, times(1)).save(id, Fixtures.photo);
        }
    }

    @Nested
    class GetPhotosTests {
        @Test
        void unauthorizedOnUnauthorized() throws Exception {
            final var id = "a";
            final var url = Routes.Offer.OFFERS + "/" + id + "/photos";

            helpers.getRequest(url).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        void getPhotos() throws Exception {
            final var id = "a";
            final var url = Routes.Offer.OFFERS + "/" + id + "/photos";
            when(service.findAllForOffer(id)).thenReturn(List.of(Fixtures.photo));

            final var result = helpers.getRequest(url).andExpect(status().isOk());
            final var expected = Routes.Offer.OFFERS + "/" + id + "/photos/" + Fixtures.photo.getId();
            assertEquals(expected, MvcHelpers.attributeFromResult("[0]", result));
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void unauthorizedOnUnauthorized() throws Exception {
            final var id = "a";
            final var photoId = "b";
            final var url = Routes.Offer.OFFERS + "/" + id + "/photos/" + photoId;

            helpers.deleteRequest(url).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        void delete() throws Exception {
            final var offerId = "a";
            final var photoId = "b";
            final var url = Routes.Offer.OFFERS + "/" + offerId + "/photos/" + photoId;

            helpers.deleteRequest(url).andExpect(status().isOk());

            verify(service, times(1)).delete(offerId, photoId);
        }

        @Test
        @WithMockUser
        void notFoundOnNotFound() throws Exception {
            final var offerId = "a";
            final var photoId = "b";
            final var url = Routes.Offer.OFFERS + "/" + offerId + "/photos/" + photoId;

            doThrow(new ApiNotFoundException("")).when(service).delete(offerId, photoId);

            helpers.deleteRequest(url).andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void badRequestOnOtherExceptionThrown() throws Exception {
            final var offerId = "a";
            final var photoId = "b";
            final var url = Routes.Offer.OFFERS + "/" + offerId + "/photos/" + photoId;

            doThrow(new ApiImageException("")).when(service).delete(offerId, photoId);

            helpers.deleteRequest(url).andExpect(status().isBadRequest());
        }
    }
}
