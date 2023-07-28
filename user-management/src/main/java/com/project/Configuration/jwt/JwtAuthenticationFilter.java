package com.project.Configuration.jwt;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Exceptions.CustomException.ValidationInput.NoJwtAuthenticationException;
import com.project.Payload.Response.ResponseEntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.Service.UserDetailServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter  extends OncePerRequestFilter{
	
	@Autowired
	private JwtServices jwtServices;
	
	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;
	
	private AuthenticationManager authenticationManager;
	
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			final String authHeader = request.getHeader("Authorization");
			final String jwt;
			final String username;
			final String requestURI = request.getRequestURI();

			if (requestURI.startsWith("/api/auth/")) {
				filterChain.doFilter(request, response);
				return;
			}

			if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				logger.error("no token");
				throw new NoJwtAuthenticationException("No jwt");
			}

				jwt = authHeader.substring(7);
			username = jwtServices.extractUsername(jwt);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails =  userDetailServiceImpl.loadUserByUsername(username);

				if (jwtServices.validateToken(jwt, userDetails)){
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							userDetails.getAuthorities()
					);
					authToken.setDetails(
							new WebAuthenticationDetailsSource().buildDetails(request)
					);
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
		        UsernamePasswordAuthenticationToken authentication = 
		            new UsernamePasswordAuthenticationToken(userDetails,
		                                                    null,
		                                                    userDetails.getAuthorities());
		        
		        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		        SecurityContextHolder.getContext().setAuthentication(authentication);
		      }
		    } catch (Exception e) {
		      logger.error("Cannot set user authentication: {}", e);
		    }

		    filterChain.doFilter(request, response);
		  }


}
