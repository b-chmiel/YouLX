package com.api.auth;

import com.api.Routes;
import com.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@Controller
@RequiredArgsConstructor
@ComponentScan(basePackages = {"com.domain"})
class AuthController {
    @Value("${frontend-url:localhost:4200}")
    private String frontendUrl;

    private final UserService service;

    @RequestMapping(Routes.Auth.LOGIN)
    ModelAndView login(HttpServletRequest request) {
        var model = new ModelAndView();
        model.setViewName("login");

        final var referrer = request.getHeader("Referrer");
        request.getSession().setAttribute("url_prior_login", referrer);

        return model;
    }

    @RequestMapping(value = Routes.Auth.REGISTER, method = RequestMethod.GET)
    ModelAndView register() {
        final var model = new ModelAndView();
        model.setViewName("register");
        return model;
    }

    @RequestMapping(value = Routes.Auth.REGISTER, method = RequestMethod.POST)
    ResponseEntity<?> register(@ModelAttribute @Valid UserRegisterDto user, BindingResult bindingResult) throws URISyntaxException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.FOUND).location(new URI(Routes.Auth.REGISTER + "?error")).build();
        }
        final var registered = service.register(user.toDomain());

        if (registered.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FOUND).location(new URI(Routes.Auth.REGISTER + "?error")).build();
        }

        return ResponseEntity.status(HttpStatus.FOUND).location(new URI(Routes.Auth.LOGIN)).build();
    }

    @RequestMapping("/")
    public RedirectView localRedirect() {
        final var redirectView = new RedirectView();
        redirectView.setUrl(frontendUrl);
        return redirectView;
    }

}
