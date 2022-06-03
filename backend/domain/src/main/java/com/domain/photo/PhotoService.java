package com.domain.photo;

import com.domain.user.UserId;
import com.domain.utils.exception.ApiException;

import java.util.List;
import java.util.Optional;

public interface PhotoService {
    void save(String offerId, Photo photo, UserId user) throws ApiException;

    List<Photo> findAllForOffer(String offerId) throws ApiException;

    void delete(String offerId, String photoId, UserId user) throws ApiException;

    Optional<Photo> find(String offerId, String photoId);

    boolean exists(String photoId);
}
