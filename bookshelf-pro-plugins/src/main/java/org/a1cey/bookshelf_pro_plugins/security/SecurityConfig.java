package org.a1cey.bookshelf_pro_plugins.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // disable for REST API
            .sessionManagement(session ->
                                   session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth ->
                                       auth
                                           // Public endpoints
                                           .requestMatchers(HttpMethod.POST, "/account").permitAll()
                                           .requestMatchers(HttpMethod.GET, "/media-item/**").permitAll()
                                           .requestMatchers(HttpMethod.GET, "/media-item/book/**").permitAll()
                                           // Swagger/OpenAPI
                                           .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                           // Everything else requires authentication
                                           .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {}); // use HTTP Basic Auth for now

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
