package com.youlx.domain.photo;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferService;
import com.youlx.domain.utils.exception.ApiException;
import com.youlx.domain.utils.exception.ApiNotFoundException;
import com.youlx.domain.utils.exception.ApiUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final OfferService offerService;
    private final PhotoRepository repository;

    @Override
    public void save(String offerId, Photo photo, String username) throws ApiException {
        if (offerService.findById(offerId).isEmpty()) {
            throw new ApiNotFoundException("Offer does not exist.");
        }
        if (!offerService.isOwnerOf(offerId, username)) {
            throw new ApiUnauthorizedException("User is not owner of offer.");
        }
        if (!isPhotoValid(photo)) {
            throw new ApiImageException("File uploaded is not photo.");
        }

        repository.savePhoto(offerId, photo);
    }

    private static boolean isPhotoValid(Photo photo) {
        try {
            if (ImageIO.read(new ByteArrayInputStream(photo.getData())) == null) {
                return false;
            }
        } catch (Exception e) {
            throw new ApiImageException("Cannot store file: " + e.getMessage());
        }

        return true;
    }

    @Override
    public List<Photo> findAllForOffer(String offerId) throws ApiException {
        return offerService
                .findById(offerId)
                .map(Offer::getPhotos)
                .orElseThrow(() -> new ApiNotFoundException("Offer not found."));
    }

    @Override
    public void delete(String offerId, String photoId, String username) throws ApiException {
        if (offerService.findById(offerId).isEmpty()) {
            throw new ApiNotFoundException("Offer not found.");
        }
        if (!offerService.isOwnerOf(offerId, username)) {
            throw new ApiUnauthorizedException("User is not owner of offer.");
        }
        if (repository.findById(photoId).isEmpty()) {
            throw new ApiNotFoundException("Photo not found.");
        }

        repository.delete(offerId, photoId);
    }

    @Override
    public Optional<Photo> find(String offerId, String photoId) {
        if (offerService.findById(offerId).isEmpty()) {
            throw new ApiNotFoundException("Offer not found.");
        }

        return repository.findById(photoId);
    }
}
