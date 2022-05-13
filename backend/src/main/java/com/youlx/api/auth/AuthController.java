package com.youlx.api.auth;

import com.youlx.api.Routes;
import com.youlx.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService service;

    @RequestMapping(Routes.Auth.LOGIN)
    public ModelAndView login() {
        var model = new ModelAndView();
        model.setViewName("login");
        return model;
    }

    @RequestMapping(value = Routes.Auth.REGISTER, method = RequestMethod.GET)
    public ModelAndView register() {
        final var model = new ModelAndView();
        model.setViewName("register");
        return model;
    }

    //TODO add validation
    @RequestMapping(value = Routes.Auth.REGISTER, method = RequestMethod.POST)
    public ResponseEntity<?> register(
            @NotNull String username,
            @NotNull String firstName,
            @NotNull String lastName,
            @NotNull String email,
            @NotNull String password
    ) throws URISyntaxException {
        final var user = new UserRegisterDto(firstName, lastName, email, username, password);
        final var registered = service.register(user.toDomain());

        if (registered.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FOUND).location(new URI(Routes.Auth.REGISTER)).build();
        }

        return ResponseEntity.status(HttpStatus.FOUND).location(new URI(Routes.Auth.LOGIN)).build();
    }
}
