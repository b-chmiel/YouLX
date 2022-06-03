package com.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.infrastructure")
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public boolean exists(String id) {
        return repository.exists(id);
    }

    @Override
    public Optional<User> register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repository.create(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findByUsername(id);
    }

    @Override
    public Optional<User> edit(String id, UserEdit user) {
        return repository.edit(id, user);
    }
}
