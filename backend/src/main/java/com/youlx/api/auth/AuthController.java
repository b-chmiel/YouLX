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

import java.net.URI;
import java.net.URISyntaxException;

@Controller
@RequiredArgsConstructor
 class AuthController {
    private final UserService service;

    @RequestMapping(Routes.Auth.LOGIN)
     ModelAndView login() {
        var model = new ModelAndView();
        model.setViewName("login");
        return model;
    }

    @RequestMapping(value = Routes.Auth.REGISTER, method = RequestMethod.GET)
     ModelAndView register() {
        final var model = new ModelAndView();
        model.setViewName("register");
        return model;
    }

    @RequestMapping(value = Routes.Auth.REGISTER, method = RequestMethod.POST)
     ResponseEntity<?> register(
            String username,
            String firstName,
            String lastName,
            String email,
            String password
    ) throws URISyntaxException {
        //TODO improve validation
        if (username == null || firstName == null || lastName == null || email == null || password == null) {
            return ResponseEntity.status(HttpStatus.FOUND).location(new URI(Routes.Auth.REGISTER + "?error")).build();
        }

        final var user = new UserRegisterDto(username, firstName, lastName, email, password);
        final var registered = service.register(user.toDomain());

        if (registered.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FOUND).location(new URI(Routes.Auth.REGISTER + "?error")).build();
        }

        return ResponseEntity.status(HttpStatus.FOUND).location(new URI(Routes.Auth.LOGIN)).build();
    }
}
