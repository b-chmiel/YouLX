package com.youlx.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${youlx.http.auth-token-header-name}")
    private String principalRequestHeader;

    @Value("${youlx.http.auth-token}")
    private String principalRequestValue;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1").password(passwordEncoder().encode("user1")).roles("USER")
                .and()
                .withUser("user2").password(passwordEncoder().encode("user2")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ApiKeySecurityConfig filter = new ApiKeySecurityConfig(principalRequestHeader);
        filter.setAuthenticationManager(authentication -> {
            String principal = (String) authentication.getPrincipal();
            if (!principalRequestValue.equals(principal)) {
                throw new BadCredentialsException("The API key was not found or not the expected value.");
            }
            authentication.setAuthenticated(true);
            return authentication;
        });

        http
                .authorizeRequests()
                .antMatchers("/api/swagger-ui/**").permitAll()
                .antMatchers("/api/swagger-ui.html").permitAll()
                .antMatchers("/api/docs").permitAll()
                .antMatchers("/login*").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(filter)
                .formLogin()
                .and()
                .logout();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}