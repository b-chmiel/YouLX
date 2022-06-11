package com.infrastructure.offer;

import com.infrastructure.user.UserTuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaOfferRepository extends JpaRepository<OfferTuple, Long> {
    List<OfferTuple> findAllByUser(UserTuple user);
}
