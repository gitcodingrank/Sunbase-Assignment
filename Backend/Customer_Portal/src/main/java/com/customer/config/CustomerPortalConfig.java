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

					cfg.setAllowedOriginPatterns(Collections.singletonList("*"));
					cfg.setAllowedMethods(Collections.singletonList("*"));
					cfg.setAllowCredentials(true);
					cfg.setAllowedHeaders(Collections.singletonList("*"));
					cfg.setExposedHeaders(Arrays.asList("Authorization"));
					return cfg;
				}
			});

		}).authorizeHttpRequests(auth ->{
			
			auth.requestMatchers(HttpMethod.POST, "/customers/register").permitAll()
			.requestMatchers(HttpMethod.GET, "/customers/signin").permitAll()
//			.requestMatchers(HttpMethod.GET, "/customers/").permitAll()
			.requestMatchers("/swagger-ui*/**","/v3/api-docs/**").permitAll()
			.requestMatchers(HttpMethod.GET, "/customers/{customerId}").hasAnyRole("ADMIN", "USER")
			.requestMatchers(HttpMethod.GET, "/customers/**").hasRole("ADMIN")
			.requestMatchers(HttpMethod.PUT, "/customers/**").hasRole("ADMIN")
			.requestMatchers(HttpMethod.DELETE, "/customers/**").hasRole("ADMIN")
			.anyRequest().authenticated();
		})
		.csrf(csrf -> csrf.disable())
		.addFilterAfter(new MyJwtTokenGenerator(), BasicAuthenticationFilter.class)
		.addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
		.formLogin(Customizer.withDefaults())
		.httpBasic(Customizer.withDefaults());
		
//		.logout(logout -> logout
//                .logoutUrl("customers/logout") // Specify the logout URL
//                .logoutSuccessUrl("/") // Specify the URL to redirect to after logout
//                .invalidateHttpSession(true) // Invalidate the HTTP session
//                .deleteCookies("JSESSIONID")); // Delete cookies upon logout
		
	
		return http.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();

	}
	
	
}
