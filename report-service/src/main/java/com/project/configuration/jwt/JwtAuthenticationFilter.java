package com.project.configuration.jwt;

import com.project.payload.dto.UserDTO;
import com.project.service.CustomUserDetail;
import com.project.service.UserDetailServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
			if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}
			jwt = authHeader.substring(7);
			username = jwtServices.extractUsername(jwt);
			Claims tokenClaims = jwtServices.validateToken(jwt);


			if (tokenClaims != null){
				UserDTO user = new UserDTO();
				user.setUsername(tokenClaims.getSubject());
				System.out.println(tokenClaims.get("role", String.class));
				user.setRoles(tokenClaims.get("role", String.class));
				UserDetails userDetails = CustomUserDetail.build(user);
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		    }
		}catch (Exception e) {
		      logger.error("Cannot set user authentication: {}", e);
		    }
		    filterChain.doFilter(request, response);
		  }

		
		
		

}
