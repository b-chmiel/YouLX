package com.youlx.api.auth;

import com.youlx.api.config.SecurityRoles;
import com.youlx.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class UserRegisterDto {
    @NotNull
    private String username;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String phone;

    User toDomain() {
        final var authorities = List.of(new SimpleGrantedAuthority(SecurityRoles.USER.name()));
        return new User(authorities, firstName, lastName, email, password, username, phone);
    }
}
