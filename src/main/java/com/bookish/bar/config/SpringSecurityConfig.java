package com.bookish.bar.config;

import com.bookish.bar.filter.JwtRequestFilter;
import com.bookish.bar.services.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfig {

    public final CustomUserDetailService customUserDetailService;
    private final JwtRequestFilter jwtRequestFilter;
    private final PasswordEncoder passwordEncoder;

    public SpringSecurityConfig(CustomUserDetailService customUserDetailService, JwtRequestFilter jwtRequestFilter, PasswordEncoder passwordEncoder) {
        this.customUserDetailService = customUserDetailService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/{id}/change-email").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/{id}/change-username").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/{id}/change-password").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasAnyAuthority("USER", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/users/{id}/authorities").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users/{id}/authorities").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}/authorities/{authority}").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/users/{id}/profile").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/users/{id}/profile").hasAuthority("USER")

                        .requestMatchers(HttpMethod.PUT, "/users/{id}/profile").hasAuthority("USER")
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}/profile").hasAnyAuthority("USER", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/profiles").permitAll()
                        .requestMatchers(HttpMethod.GET, "/profiles/{username}").permitAll()

                        .requestMatchers(HttpMethod.POST, "/users/{id}/profile/photo").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/users/{id}/profile/photo/download/{filename}").hasAuthority("USER")
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}/profile/photo").hasAnyAuthority("USER", "ADMIN")


                        .requestMatchers("/authenticated").authenticated()
                        .requestMatchers("/authenticate").permitAll()
                        .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}
