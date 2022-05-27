package com.youlx.infrastructure.photo;

import com.youlx.domain.photo.Photo;
import com.youlx.domain.photo.PhotoRepository;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.domain.utils.ApiException;
import com.youlx.domain.photo.ApiImageException;
import com.youlx.domain.utils.ApiNotFoundException;
import com.youlx.infrastructure.offer.OfferTuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@RequiredArgsConstructor
public class PhotoRepositoryImpl implements PhotoRepository {
    public interface Repo extends JpaRepository<PhotoTuple, Long> {
    }

    public interface OfferRepo extends JpaRepository<OfferTuple, Long> {
    }

    private final Repo repo;
    private final OfferRepo offerRepo;
    private final HashId hashId;

    @Override
    public Photo savePhoto(String offerId, Photo photo) throws ApiException {
        final var decoded = hashId.decode(offerId);

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
}
