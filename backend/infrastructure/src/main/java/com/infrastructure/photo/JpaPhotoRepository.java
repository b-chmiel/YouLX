package com.infrastructure.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPhotoRepository extends JpaRepository<PhotoTuple, Long> {
}

