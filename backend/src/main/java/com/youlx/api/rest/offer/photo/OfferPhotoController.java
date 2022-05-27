package com.youlx.api.rest.offer.photo;

import com.youlx.api.Routes;
import com.youlx.domain.photo.Photo;
import com.youlx.domain.photo.PhotoService;
import com.youlx.domain.utils.ApiException;
import com.youlx.domain.utils.ApiNotFoundException;
import com.youlx.domain.utils.uuid.Uuid;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.Offer.OFFERS)
class OfferPhotoController {
    private final PhotoService service;
    private final Uuid uuid;

    @PostMapping(value = "{offerId}/photos", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<?> postPhoto(
            @Valid @PathVariable String offerId,
            @Parameter(content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)) @RequestParam("file") MultipartFile file
    ) {
        try {
            service.save(offerId, new Photo(uuid, file));
            return ResponseEntity.ok(null);
        } catch (ApiNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{offerId}/photos")
    ResponseEntity<?> getPhotos(@Valid @PathVariable String offerId) {
        try {
            final var result = service
                    .findAllForOffer(offerId)
                    .stream()
                    .map(p -> Routes.Offer.OFFERS + "/" + offerId + "/photos/" + p.getId())
                    .toList();
            return ResponseEntity.ok(result);
        } catch (ApiNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "{offerId}/photos/{photoId}", produces = MediaType.IMAGE_JPEG_VALUE)
    void getPhoto(HttpServletResponse response) throws IOException {
        final var photo = new ClassPathResource("photos/cat.jpg");
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(photo.getInputStream(), response.getOutputStream());
    }

    @DeleteMapping("{offerId}/photos/{photoId}")
    ResponseEntity<?> deletePhoto(@Valid @PathVariable String offerId, @Valid @PathVariable String photoId) {
        try {
            service.delete(offerId, photoId);
            return ResponseEntity.ok().build();
        } catch (ApiNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
