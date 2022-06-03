package com.infrastructure.photo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPhotoRepository extends JpaRepository<PhotoTuple, Long> {
}

