package com.infrastructure.user;

import com.domain.user.User;
import com.domain.user.UserEdit;
import com.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository repo;

    @Override
    public Optional<User> findByUsername(String username) {
        return repo.findById(username).map(UserTuple::toDomain);
    }

    @Override
    public Optional<User> create(User user) {
        if (findByUsername(user.getUsername()).isPresent()) {
            return Optional.empty();
        }

        return Optional.of(repo.save(new UserTuple(user)).toDomain());
    }

    @Override
    public void clear() {
        repo.deleteAll();
    }

    @Override
    public Optional<User> edit(String id, UserEdit user) {
        final var toEdit = repo.findById(id);
        if (toEdit.isEmpty()) {
            return Optional.empty();
        }

        toEdit.get().setEmail(user.email());
        toEdit.get().setFirstName(user.firstName());
        toEdit.get().setLastName(user.lastName());
        toEdit.get().setPhone(user.phone());

        return Optional.of(repo.save(toEdit.get()).toDomain());
    }

    @Override
    public boolean exists(String id) {
        return repo.existsById(id);
    }
}
