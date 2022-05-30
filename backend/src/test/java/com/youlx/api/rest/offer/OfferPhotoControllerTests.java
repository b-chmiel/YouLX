package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.photo.ApiImageException;
import com.youlx.domain.photo.PhotoService;
import com.youlx.domain.utils.exception.ApiNotFoundException;
import com.youlx.domain.utils.exception.ApiUnauthorizedException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OfferPhotoControllerTests {
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
            final var file = new MockMultipartFile("file", "index.jpg", MediaType.IMAGE_JPEG_VALUE, Fixtures.photo.getData());
            helpers.postFile(file, Routes.Offer.OFFERS + "/" + id + "/photos").andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser("user")
        void userIsNotOwner() throws Exception {
            final var id = "a";
            when(uuid.generate()).thenReturn(Fixtures.photo.getId());
            doThrow(new ApiUnauthorizedException("")).when(service).save(id, Fixtures.photo, "user");
            final var file = new MockMultipartFile("file", "index.jpg", MediaType.IMAGE_JPEG_VALUE, Fixtures.photo.getData());
            helpers.postFile(file, Routes.Offer.OFFERS + "/" + id + "/photos").andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser("user")
        void offerNotFound() throws Exception {
            final var id = "a";
            when(uuid.generate()).thenReturn(Fixtures.photo.getId());
            doThrow(new ApiNotFoundException("")).when(service).save(id, Fixtures.photo, "user");
            final var file = new MockMultipartFile("file", "index.jpg", MediaType.IMAGE_JPEG_VALUE, Fixtures.photo.getData());

            helpers.postFile(file, Routes.Offer.OFFERS + "/" + id + "/photos").andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser("user")
        void post() throws Exception {
            final var id = "a";
            final var url = Routes.Offer.OFFERS + "/" + id + "/photos";

            when(uuid.generate()).thenReturn(Fixtures.photo.getId());
            final var file = new MockMultipartFile("file", "index.jpg", MediaType.IMAGE_JPEG_VALUE, Fixtures.photo.getData());

            helpers.postFile(file, url).andExpect(status().isOk());
            verify(service, times(1)).save(id, Fixtures.photo, "user");
        }
    }

    @Nested
    class GetPhotosTests {
        @Test
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
        @WithMockUser("user")
        void delete() throws Exception {
            final var offerId = "a";
            final var photoId = "b";
            final var url = Routes.Offer.OFFERS + "/" + offerId + "/photos/" + photoId;

            helpers.deleteRequest(url).andExpect(status().isOk());

            verify(service, times(1)).delete(offerId, photoId, "user");
        }

        @Test
        @WithMockUser("user")
        void offerNotFound() throws Exception {
            final var offerId = "a";
            final var photoId = "b";
            final var url = Routes.Offer.OFFERS + "/" + offerId + "/photos/" + photoId;

            doThrow(new ApiNotFoundException("")).when(service).delete(offerId, photoId, "user");

            helpers.deleteRequest(url).andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser("user")
        void userIsNotOwner() throws Exception {
            final var offerId = "a";
            final var photoId = "b";
            final var url = Routes.Offer.OFFERS + "/" + offerId + "/photos/" + photoId;

            doThrow(new ApiUnauthorizedException("")).when(service).delete(offerId, photoId, "user");

            helpers.deleteRequest(url).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser("user")
        void badRequestOnOtherExceptionThrown() throws Exception {
            final var offerId = "a";
            final var photoId = "b";
            final var url = Routes.Offer.OFFERS + "/" + offerId + "/photos/" + photoId;

            doThrow(new ApiImageException("")).when(service).delete(offerId, photoId, "user");

            helpers.deleteRequest(url).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class GetTests {
        @Test
        void notFound() throws Exception {
            final var offerId = "a";
            final var photoId = "b";
            final var url = Routes.Offer.OFFERS + "/" + offerId + "/photos/" + photoId;
            helpers.getRequest(url).andExpect(status().isNotFound());
        }

        @Test
        void get() throws Exception {
            final var offerId = "a";
            final var photoId = "b";
            final var url = Routes.Offer.OFFERS + "/" + offerId + "/photos/" + photoId;
            when(service.find(offerId, photoId)).thenReturn(Optional.of(Fixtures.photo));
            helpers.getRequest(url).andExpect(status().isOk());
        }
    }
}
