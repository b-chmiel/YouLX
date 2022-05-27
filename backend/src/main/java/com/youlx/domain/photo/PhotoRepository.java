package com.youlx.domain.photo;

public interface PhotoRepository {
    Photo savePhoto(String offerId, Photo photo) throws Exception;
}
