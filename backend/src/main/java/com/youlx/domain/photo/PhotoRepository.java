package com.youlx.domain.photo;

import com.youlx.domain.utils.ApiException;

public interface PhotoRepository {
    Photo savePhoto(String offerId, Photo photo) throws ApiException;
}
