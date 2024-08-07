package com.customer.config;

import java.io.IOException;


import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyJwtTokenGenerator extends OncePerRequestFilter{
	
	
	public MyJwtTokenGenerator() {
		// TODO Auto-generated constructor stub
		log.info("Called Constructor");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
        if (null != authentication) {

        	log.info("auth.getAuthorities "+authentication.getAuthorities());

            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes());
            
            String jwt = Jwts.builder()
            		.setIssuer(authentication.getName())
            		.setSubject("JWT Token")
                    .claim("username", authentication.getName())
                    .claim("authorities", populateAuthorities(authentication.getAuthorities()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime()+ 360000)) // expiration time of 1 minute
                    .signWith(key).compact();
            
            //System.out.println(jwt);
            log.info("jwt ",jwt);

            response.setHeader(SecurityConstants.JWT_HEADER, jwt);

        }

        filterChain.doFilter(request, response);
		
	}
	
	private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {

    	Set<String> authoritiesSet = new HashSet<>();

        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);

    }
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		
		return request.getServletPath().equals("customers/signin") ;
	}

}
