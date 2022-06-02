package com.youlx.infrastructure.photo;

import com.youlx.domain.photo.ApiImageException;
import com.youlx.domain.photo.Photo;
import com.youlx.domain.photo.PhotoRepository;
import com.youlx.domain.utils.exception.ApiConflictException;
import com.youlx.domain.utils.exception.ApiException;
import com.youlx.domain.utils.exception.ApiNotFoundException;
import com.youlx.domain.utils.hashId.ApiHashIdException;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.offer.JpaOfferRepository;
import com.youlx.infrastructure.offer.OfferTuple;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class PhotoRepositoryImpl implements PhotoRepository {
    private final HashId hashId;
    private final JpaPhotoRepository repo;
    private final JpaOfferRepository offerRepo;

    @Override
    @Transactional
    public Photo savePhoto(String offerId, Photo photo) throws ApiException {
        if (photo == null) {
            throw new ApiImageException("Image cannot be null.");
        }

        if (exists(photo.getId())) {
            throw new ApiConflictException("Image already exists.");
        }

        final Long decoded;
        try {
            decoded = hashId.decode(offerId);
        } catch (ApiHashIdException e) {
            throw new ApiNotFoundException("Offer not found: " + e.getMessage());
        }

        final PhotoTuple photoTuple;
        try {
            photoTuple = new PhotoTuple(photo);
        } catch (IOException e) {
            throw new ApiImageException("Could not save image to database: " + e.getMessage());
        } catch (NullPointerException e) {
            throw new ApiImageException("Image cannot be null: " + e.getMessage());
        }

        final var savedPhoto = repo.save(photoTuple);

        try {
            final var offer = offerRepo.getById(decoded);
            final var photos = offer.getPhotos();
            photos.add(savedPhoto);
            offerRepo.save(offer);
        } catch (EntityNotFoundException e) {
            throw new ApiNotFoundException("Could not find entity with id: " + decoded);
        }

        try {
            return savedPhoto.toDomain(hashId);
        } catch (NullPointerException e) {
            throw new ApiImageException("Cannot transform photo from null data: " + e.getMessage());
        }
    }

    @Override
    public Optional<Photo> findById(String photoId) throws ApiException {
        final Long id;
        try {
            id = hashId.decode(photoId);
        } catch (ApiHashIdException e) {
            throw new ApiNotFoundException("Photo not found: " + e.getMessage());
        }

        return repo.findById(id).map(p -> p.toDomain(hashId));
    }

    @Override
    public void delete(String offerId, String photoId) {
        final Optional<OfferTuple> offer;
        try {
            offer = offerRepo.findById(hashId.decode(offerId));
        } catch (ApiHashIdException e) {
            throw new ApiNotFoundException("Offer not found: " + e.getMessage());
        }

        if (offer.isEmpty()) {
            throw new ApiNotFoundException("Offer not found.");
        }

        final Long id;
        try {
            id = hashId.decode(photoId);
        } catch (ApiHashIdException e) {
            throw new ApiNotFoundException("Photo not found: " + e.getMessage());
        }

        if (!offer.get().getPhotos().removeIf(p -> p.getId().equals(id))) {
            throw new ApiNotFoundException("Photo not found.");
        }

        offerRepo.save(offer.get());
    }

    @Override
    public boolean exists(String photoId) {
        final Long id;
        try {
            id = hashId.decode(photoId);
        } catch (ApiHashIdException e) {
            return false;
        }

        return repo.existsById(id);
    }
}
