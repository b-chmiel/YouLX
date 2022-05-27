package com.youlx.domain.photo;

import com.youlx.domain.utils.ApiException;

import java.util.List;

public interface PhotoService {
    void save(String offerId, Photo photo) throws ApiException;
    List<Photo> findAllForOffer(String offerId) throws ApiException;
}
