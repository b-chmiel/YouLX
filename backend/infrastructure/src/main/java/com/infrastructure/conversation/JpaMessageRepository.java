package com.infrastructure.conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMessageRepository extends JpaRepository<MessageTuple, Long> {
}
