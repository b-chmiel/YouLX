package com.youlx.domain.photo;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferFindService;
import com.youlx.domain.offer.OfferStateCheckService;
import com.youlx.domain.user.UserId;
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
    private final OfferFindService offerFindService;
    private final PhotoRepository repository;
    private final OfferStateCheckService offerStateCheckService;

    @Override
    public void save(String offerId, Photo photo, UserId user) throws ApiException {
        if (!offerFindService.exists(offerId)) {
            throw new ApiNotFoundException("Offer does not exist.");
        }
        if (!offerStateCheckService.isOwnerOf(offerId, user)) {
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
        return offerFindService
                .findById(offerId)
                .map(Offer::getPhotos)
                .orElseThrow(() -> new ApiNotFoundException("Offer not found."));
    }

    @Override
    public void delete(String offerId, String photoId, UserId user) throws ApiException {
        if (!offerFindService.exists(offerId)) {
            throw new ApiNotFoundException("Offer not found.");
        }
        if (!offerStateCheckService.isOwnerOf(offerId, user)) {
            throw new ApiUnauthorizedException("User is not owner of offer.");
        }
        if (!exists(photoId)) {
            throw new ApiNotFoundException("Photo not found.");
        }

        repository.delete(offerId, photoId);
    }

    @Override
    public Optional<Photo> find(String offerId, String photoId) {
        if (!offerFindService.exists(offerId)) {
            throw new ApiNotFoundException("Offer not found.");
        }

        return repository.findById(photoId);
    }

    @Override
    public boolean exists(String photoId) {
        return repository.exists(photoId);
    }
}
