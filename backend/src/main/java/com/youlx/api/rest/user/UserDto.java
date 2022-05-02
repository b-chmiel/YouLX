package com.youlx.api.rest.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String login;
}
