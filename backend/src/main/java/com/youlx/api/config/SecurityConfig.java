package com.youlx.api.config;

import com.youlx.api.Routes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
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
        http
                .csrf().disable()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(
                        new Http403ForbiddenEntryPoint(),
                        new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"))
                .and()
                .authorizeRequests()
                .antMatchers("/api/swagger-ui/**").permitAll()
                .antMatchers("/api/swagger-ui.html").permitAll()
                .antMatchers("/api/docs").permitAll()
                .antMatchers(Routes.Auth.LOGIN).permitAll()
                .antMatchers(Routes.Auth.LOGIN + "/*").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers(Routes.Offer.OFFERS).permitAll()
                .antMatchers(Routes.Offer.OFFERS + "/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage(Routes.Auth.LOGIN)
                .loginProcessingUrl(Routes.Auth.LOGIN)
                .and()
                .logout()
                .logoutUrl(Routes.Auth.LOGOUT)
                .permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}