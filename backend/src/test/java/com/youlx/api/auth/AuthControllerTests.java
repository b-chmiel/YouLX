package com.youlx.api.auth;

import com.youlx.api.Routes;
import com.youlx.testUtils.MvcHelpers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTests {
    @Autowired
    private MvcHelpers helpers;

    @Nested
    class LoginTests {
        @Test
        void loginReturns200IfNotAuthenticated() throws Exception {
            helpers.getRequest(Routes.Auth.LOGIN).andExpect(status().isOk());
        }

        @Test
        void shouldReturnErrorOnWrongCreds() throws Exception {
            final var loginParams = new HashMap<String, String>();
            loginParams.put("username", "username");
            loginParams.put("password", "password");
            helpers.postFormRequest(loginParams, Routes.Auth.LOGIN).andExpect(redirectedUrl(Routes.Auth.LOGIN + "?error"));
        }

        @Test
        void shouldLogin() throws Exception {
            final var loginParams = new HashMap<String, String>();
            loginParams.put("username", "user1");
            loginParams.put("password", "user1");
            helpers.postFormRequest(loginParams, Routes.Auth.LOGIN).andExpect(redirectedUrl("/"));
        }
    }

    @Nested
    class RegisterTests {
        @Test
        void registerReturns200IfNotAuthenticated() throws Exception {
            helpers.getRequest(Routes.Auth.REGISTER).andExpect(status().isOk());
        }

        @Test
        void registerValidation() throws Exception {
            final var params = new HashMap<String, String>();

            helpers.postFormRequest(params, Routes.Auth.REGISTER).andExpect(redirectedUrl(Routes.Auth.REGISTER + "?error"));
        }

        @Test
        void registerAndLogin() throws Exception {
            final var params = new HashMap<String, String>();
            params.put("username", "a");
            params.put("firstName", "b");
            params.put("lastName", "c");
            params.put("email", "d");
            params.put("password", "e");
            params.put("phone", "f");

            helpers.postFormRequest(params, Routes.Auth.REGISTER).andExpect(redirectedUrl(Routes.Auth.LOGIN));

            final var loginParams = new HashMap<String, String>();
            loginParams.put("username", params.get("username"));
            loginParams.put("password", params.get("password"));
            helpers.postFormRequest(loginParams, Routes.Auth.LOGIN).andExpect(redirectedUrl("/"));
        }
    }
}
