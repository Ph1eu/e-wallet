package com.user_management.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.user_management.Configuration.jwt.JwtAuthenticationFilter;
import com.user_management.Configuration.jwt.JwtEntryPoint;
import com.user_management.Configuration.jwt.JwtProperties;
import com.user_management.Repository.RoleRepository;
import com.user_management.Repository.UserRepository;
import com.user_management.Service.CustomUserDetail;
import com.user_management.Service.UserDetailServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity()

public class WebsecurityConfig {

    @Autowired
    UserDetailServiceImpl userDetailsService;
    @Autowired
    JwtEntryPoint unauthorizedHandler;
    
	@Autowired
	JwtProperties jwtProperties;

    @Bean
    public JwtAuthenticationFilter authenticationJwtTokenFilter() {
      return new JwtAuthenticationFilter(null);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        //create database authentication provider
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // create Bscrypt encoder
        return new BCryptPasswordEncoder();
    }
    
    
    public AuthenticationSuccessHandler successHandler() {
    	return (request, response, exception) -> {
    		
    		
 		    CustomUserDetail userDetails = (CustomUserDetail)userDetailsService.loadUserByUsername(request.getParameter("username"));

 		    ResponseCookie jwtCookie = jwtProperties.generateJwtCookie(userDetails);
 		 // Prepare the response data
 	        Map<String, Object> responseData = new HashMap<>();
 	        responseData.put("id_email", userDetails.getEmail());
 	        responseData.put("username", userDetails.getUsername());
 	        responseData.put("email", userDetails.getEmail());
 	        responseData.put("role", userDetails.getRole());
 		    
 	        response.setHeader("Authorization", "Bearer " + jwtCookie); 
 	     // Convert the response data to JSON
 	        String jsonResponse = new ObjectMapper().writeValueAsString(responseData);
 	     // Set the response content type and write the JSON response
 	        response.setContentType("application/json");
 	        response.setCharacterEncoding("UTF-8");
 	        response.getWriter().write(jsonResponse);
        };
    }
    private AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, exception) -> {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("Failed login");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        };
    }
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
    http
 // enable cors,remove csrf and state in session because in jwt we do not need them
    .cors().and().csrf().disable() 
    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    .and().formLogin()
    .successHandler(successHandler())
    .failureHandler(loginFailureHandler())
    .and()
    .logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout").//and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).
        and().authorizeHttpRequests().
            requestMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated();

    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return  http.build();
//  }


}
}
