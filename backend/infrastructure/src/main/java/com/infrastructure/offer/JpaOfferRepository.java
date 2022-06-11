package com.infrastructure.offer;

import com.infrastructure.user.UserTuple;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOfferRepository extends JpaRepository<OfferTuple, Long> {
    List<OfferTuple> findAllByUser(UserTuple user);
}
