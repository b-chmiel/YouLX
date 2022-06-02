package com.youlx.infrastructure.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserTuple, String> {
}
