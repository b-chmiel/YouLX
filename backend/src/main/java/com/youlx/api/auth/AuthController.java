package com.youlx.api.auth;

import com.youlx.api.Routes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {
    @RequestMapping(Routes.Auth.LOGIN)
    public ModelAndView login() {
        var model = new ModelAndView();
        model.setViewName("login");
        return model;
    }
}
