package com.youlx.infrastructure.user;

import com.youlx.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Table(name = "LX_USER")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
class UserTuple {
    @Id
    private String username;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authorities;

    UserTuple(User user) {
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }

    User toDomain() {
        final var auth = authorities.stream().map(SimpleGrantedAuthority::new).toList();
        return new User(auth, firstName, lastName, email, password, username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTuple userTuple = (UserTuple) o;
        return Objects.equals(username, userTuple.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
