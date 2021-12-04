package com.application.restaurant.configuration;

import com.application.restaurant.model.UserRoles;
import com.application.restaurant.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationSuccessHandler successHandler;

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/registration/**").permitAll()
                    .antMatchers("/confirm/**").permitAll()
                    .antMatchers("/process_register").permitAll()
                    .antMatchers("/homepage/**").permitAll()
                .antMatchers("/api/v*/admin/**").permitAll()
                .antMatchers("/api/v*/waiter/**").permitAll()
//                    .antMatchers("/api/v*/admin/**").access("hasRole('ROLE_ADMIN')")
//                    .antMatchers("/api/v*/registered_user/**").access("hasRole('ROLE_REGISTERED_USER')")
//                    .antMatchers("/api/v*/waiter/**").access("hasRole('ROLE_WAITER')")
                .anyRequest()
                .authenticated()
                .and().formLogin()
                .successHandler(successHandler)
                .permitAll()
                .and().logout()
                .logoutUrl("/api/v*/**/logout");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);

        return provider;
    }
}
