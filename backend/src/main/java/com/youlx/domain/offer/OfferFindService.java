package com.youlx.domain.offer;

import com.youlx.domain.user.UserShallow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OfferFindService {
    boolean exists(String id);

    Optional<Offer> findById(String id);

    Page<Offer> findBy(Pageable pageable, UserShallow user, String status, String tag);

    Page<Offer> findOpen(Pageable pageable, UserShallow user, String tags);

    List<Offer> search(UserShallow user, String query);
}
