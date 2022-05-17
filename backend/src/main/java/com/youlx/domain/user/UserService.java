package com.youlx.domain.user;

import java.util.Optional;

public interface UserService {
    Optional<User> register(User user);
    Optional<User> findById(String id);
}
