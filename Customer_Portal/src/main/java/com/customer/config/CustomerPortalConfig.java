package com.customer.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class CustomerPortalConfig {

    @Bean
    public SecurityFilterChain springSecurityConfiguration(HttpSecurity http) throws Exception {
        
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(cors -> {
                cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration cfg = new CorsConfiguration();
                        cfg.setAllowedOriginPatterns(Collections.singletonList("*")); // Consider restricting this for security
                        cfg.setAllowedMethods(Collections.singletonList("*"));
                        cfg.setAllowCredentials(true);
                        cfg.setAllowedHeaders(Collections.singletonList("*"));
                        cfg.setExposedHeaders(Arrays.asList("Authorization"));
                        return cfg;
                    }
                });
            })
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(HttpMethod.POST, "/customers/register").permitAll()
                    .requestMatchers(HttpMethod.GET, "/customers/signin", "/customers/**").permitAll()
                    .requestMatchers("/swagger-ui*/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/customers/**").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/customers/**").permitAll()
//                    .requestMatchers(HttpMethod.GET, "/customers/{customerId}").hasAnyRole("ADMIN", "USER")
       
//                    .requestMatchers(HttpMethod.GET, "/customers/**").hasAnyRole("ADMIN", "USER")
//                    .requestMatchers(HttpMethod.PUT, "/customers/**").hasAnyRole("ADMIN", "USER")
//                    .requestMatchers(HttpMethod.DELETE, "/customers/**").hasAnyRole("ADMIN", "USER")
                    .anyRequest().authenticated();
            })
            .csrf(csrf -> csrf.disable())
            .addFilterAfter(new MyJwtTokenGenerator(), BasicAuthenticationFilter.class)
            .addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
