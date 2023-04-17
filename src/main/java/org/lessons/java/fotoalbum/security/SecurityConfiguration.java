package org.lessons.java.fotoalbum.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration

public class SecurityConfiguration {

    @Bean
    UserDetailsService userDetailsService() {
        return new DatabaseUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        //creo un authentication provider basato su database (DAO)
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //Associo il mio DatabaseUserDetailsService
        provider.setUserDetailsService(userDetailsService());
        //associo il mio PasswordEncoder
        provider.setPasswordEncoder(passwordEncoder());
        //lo ritorno
        return provider;

    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/categories", "/categories/**").hasAuthority("ADMIN")
                .requestMatchers("/photo/create", "/photo/edit/**", "/photo/delete/**")
                .hasAuthority("ADMIN")
                .requestMatchers("/", "/photo", "/photo/**").hasAnyAuthority("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/photo/**").hasAuthority("ADMIN")
                .requestMatchers("/**").permitAll()
                .and().formLogin()
                .and().logout()
                .and().exceptionHandling();
        http.csrf().disable();
        return http.build();
    }
}