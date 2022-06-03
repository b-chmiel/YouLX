package com.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class UserId {
    private final String username;

    public UserId() {
        this.username = null;
    }

    public UserId(Principal user) {
        this.username = user == null ? null : user.getName();
    }

    public UserId(User user) {
        this.username = user == null ? null : user.getUsername();
    }
}
