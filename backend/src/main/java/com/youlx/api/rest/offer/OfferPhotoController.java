package com.youlx.api.rest.offer;

import com.youlx.api.Routes;
import com.youlx.domain.photo.Photo;
import com.youlx.domain.photo.PhotoService;
import com.youlx.domain.user.UserId;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.Offer.OFFERS)
class OfferPhotoController {
    private final PhotoService service;

    @PostMapping(value = "{offerId}/photos", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<Void> postPhoto(
            Principal user,
            @Valid @PathVariable String offerId,
            @Parameter(content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)) @RequestParam("file") MultipartFile file
    ) throws IOException {
        service.save(offerId, new Photo(file), new UserId(user));
        return ResponseEntity.ok().build();
    }

    @GetMapping("{offerId}/photos")
    ResponseEntity<List<String>> getPhotos(@Valid @PathVariable String offerId) {
        final var result = service
                .findAllForOffer(offerId)
                .stream()
                .map(p -> Routes.Offer.OFFERS + "/" + offerId + "/photos/" + p.getId())
                .toList();
        return ResponseEntity.ok(result);
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
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<Void> deletePhoto(Principal user, @Valid @PathVariable String offerId, @Valid @PathVariable String photoId) {
        service.delete(offerId, photoId, new UserId(user));
        return ResponseEntity.ok().build();
    }
}
