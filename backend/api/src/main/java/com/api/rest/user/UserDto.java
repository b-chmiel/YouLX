package com.api.rest.user;

import com.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String login;
    private String phone;

    public UserDto(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.login = user.getUsername();
        this.phone = user.getPhone();
    }
}
