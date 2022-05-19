package com.youlx.domain.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    Optional<User> create(User user);

    void clear();

    Optional<User> edit(String id, UserEdit user);
}
