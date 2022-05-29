package com.youlx.domain.user;

import java.util.Optional;

public interface UserService {
    boolean exists(String id);

    Optional<User> register(User user);

    Optional<User> findById(String id);

    Optional<User> edit(String id, UserEdit user);
}
