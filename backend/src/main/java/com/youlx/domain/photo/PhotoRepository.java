package com.youlx.domain.photo;

import com.youlx.domain.utils.exception.ApiException;

import java.util.Optional;

public interface PhotoRepository {
    Photo savePhoto(String offerId, Photo photo) throws ApiException;

    Optional<Photo> findById(String id);

    void delete(String offerId, String photoId) throws ApiException;
    boolean exists(String photoId);
}
