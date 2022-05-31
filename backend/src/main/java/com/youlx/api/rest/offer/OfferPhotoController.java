package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.photo.Photo;
import com.youlx.domain.photo.PhotoService;
import com.youlx.domain.user.UserId;
import com.youlx.domain.utils.exception.ApiConflictException;
import com.youlx.domain.utils.exception.ApiException;
import com.youlx.domain.utils.exception.ApiNotFoundException;
import com.youlx.domain.utils.exception.ApiUnauthorizedException;
import com.youlx.domain.utils.uuid.Uuid;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.Offer.OFFERS)
class OfferPhotoController {
    private final PhotoService service;
    private final Uuid uuid;

    @PostMapping(value = "{offerId}/photos", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<?> postPhoto(
            Principal user,
            @Valid @PathVariable String offerId,
            @Parameter(content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)) @RequestParam("file") MultipartFile file
    ) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            service.save(offerId, new Photo(file), new UserId(user));
            return ResponseEntity.ok(null);
        } catch (ApiNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ApiUnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (ApiConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
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
    void getPhoto(@Valid @PathVariable String offerId, @Valid @PathVariable String photoId, HttpServletResponse response) throws IOException {
        final var photo = service.find(offerId, photoId);
        if (photo.isPresent()) {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(photo.get().getData(), response.getOutputStream());
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }

    @DeleteMapping("{offerId}/photos/{photoId}")
    ResponseEntity<?> deletePhoto(Principal user, @Valid @PathVariable String offerId, @Valid @PathVariable String photoId) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            service.delete(offerId, photoId, new UserId(user));
            return ResponseEntity.ok().build();
        } catch (ApiNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ApiUnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
