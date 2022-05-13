package com.youlx.api.auth;

import com.youlx.api.config.SecurityRoles;
import com.youlx.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
class UserRegisterDto {
    private String firstName;
    private String lastName;
    private String email;
    private String login;
    private String password;

    User toDomain() {
        final var authorities = List.of(new SimpleGrantedAuthority(SecurityRoles.USER.name()));
        return new User(authorities, firstName, lastName, email, password, login);
    }
}
