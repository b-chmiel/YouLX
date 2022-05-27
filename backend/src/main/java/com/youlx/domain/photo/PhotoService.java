package com.youlx.domain.photo;

import com.youlx.domain.utils.ApiException;

import java.util.List;
import java.util.Optional;

public interface PhotoService {
    void save(String offerId, Photo photo) throws ApiException;
    List<Photo> findAllForOffer(String offerId) throws ApiException;

    void delete(String offerId, String photoId) throws ApiException;

    Optional<Photo> find(String offerId, String photoId);
}
