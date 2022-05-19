package com.youlx.infrastructure.user;

import com.youlx.domain.user.User;
import com.youlx.domain.user.UserEdit;
import com.youlx.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    public interface Repo extends JpaRepository<UserTuple, String> {
        Optional<UserTuple> findById(String s);
    }

    private final Repo repo;

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

        return Optional.of(repo.save(toEdit.get()).toDomain());
    }
}
