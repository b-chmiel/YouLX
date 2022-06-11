package com.infrastructure.conversation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMessageRepository extends JpaRepository<MessageTuple, Long> {
}
