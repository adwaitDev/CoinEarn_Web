package com.adwait.aitrading.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class AppConfig {

    // Reads the property above; splits into a List<String>
    @Value("${app.cors.allowed-origins}")
    private List<String> allowedOrigins;

    // NEW: inject the JwtConstant bean
    private final JwtConstant jwtConstant;

    // NEW: constructor injection
    public AppConfig(JwtConstant jwtConstant) {
        this.jwtConstant = jwtConstant;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // 1. Allow H2 console
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                // CHANGED: pass jwtConstant into the constructor
                .addFilterBefore(new JwtTokenValidator(jwtConstant), BasicAuthenticationFilter.class)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**") // 2. Disable CSRF for H2
                        .disable()
                )
                // 3. Allow iframes from same origin (fixes the broken UI)
                .headers(headers -> headers
                        .frameOptions(frame -> frame
                                .sameOrigin()
                        )
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}