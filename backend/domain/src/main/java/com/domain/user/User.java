package com.domain.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class User implements UserDetails {
    private final Collection<? extends GrantedAuthority> authorities;
    private final String firstName;
    private final String lastName;
    private final String email;

    @Setter
    private String password;

    private final String username;
    private final String phone;

    public User(Collection<? extends GrantedAuthority> authorities, String firstName, String lastName, String email, String password, String username, String phone) {
        this.authorities = authorities;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
        this.phone = phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
