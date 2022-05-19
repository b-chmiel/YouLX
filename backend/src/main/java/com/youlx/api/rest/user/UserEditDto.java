package com.youlx.api.rest.user;

import com.youlx.domain.user.UserEdit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
class UserEditDto {
    private String firstName;
    private String lastName;
    private String email;

    UserEdit toDomain() {
        return new UserEdit(firstName, lastName, email);
    }
}
