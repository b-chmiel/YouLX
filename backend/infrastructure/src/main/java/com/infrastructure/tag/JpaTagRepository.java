package com.infrastructure.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaTagRepository extends JpaRepository<TagTuple, Long> {
    boolean existsByName(String name);

    Optional<TagTuple> findByName(String name);
}

