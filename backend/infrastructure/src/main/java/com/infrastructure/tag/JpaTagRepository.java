package com.infrastructure.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaTagRepository extends JpaRepository<TagTuple, Long> {
    boolean existsByName(String name);

    Optional<TagTuple> findByName(String name);
}

