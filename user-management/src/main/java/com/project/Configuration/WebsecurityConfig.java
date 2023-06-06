package com.project.Configuration;

import com.project.Configuration.jwt.JwtAuthenticationFilter;
import com.project.Configuration.jwt.JwtEntryPoint;
import com.project.Configuration.jwt.JwtServices;
import com.project.Service.UserDetailServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity()

public class WebsecurityConfig {

    @Autowired
    UserDetailServiceImpl userDetailsService;
    @Autowired
    JwtEntryPoint unauthorizedHandler;
    
	@Autowired
    JwtServices jwtServices;

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
    
//    
//    public AuthenticationSuccessHandler successHandler() {
//    	return (request, response, exception) -> {
//    		
//    		
// 		    CustomUserDetail userDetails = (CustomUserDetail)userDetailsService.loadUserByUsername(request.getParameter("username"));
//
// 		    ResponseCookie jwtCookie = jwtProperties.generateJwtCookie(userDetails);
// 		 // Prepare the response data
// 	        Map<String, Object> responseData = new HashMap<>();
// 	        responseData.put("id_email", userDetails.getEmail());
// 	        responseData.put("username", userDetails.getUsername());
// 	        responseData.put("email", userDetails.getEmail());
// 	        responseData.put("role", userDetails.getRole());
// 		    
// 	        response.setHeader("Authorization", "Bearer " + jwtCookie); 
// 	     // Convert the response data to JSON
// 	        String jsonResponse = new ObjectMapper().writeValueAsString(responseData);
// 	     // Set the response content type and write the JSON response
// 	        response.setContentType("application/json");
// 	        response.setCharacterEncoding("UTF-8");
// 	        response.getWriter().write(jsonResponse);
//        };
//    }
//    private AuthenticationFailureHandler loginFailureHandler() {
//        return (request, response, exception) -> {
//            response.setContentType("text/html;charset=UTF-8");
//            response.getWriter().write("Failed login");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        };
//    }
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
    http
 // enable cors,remove csrf and state in session because in jwt we do not need them
    .cors().and().csrf().disable()
    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    //Authorize signup for all people
   .and().authorizeHttpRequests().
   requestMatchers("/api/auth/**").permitAll()
   .requestMatchers("/api/admin/**").hasRole("ADMIN")
   .requestMatchers("/api/user/**").hasAnyRole("ADMIN","USER")
   .anyRequest().authenticated().and().
   formLogin().disable();
   
    
    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return  http.build();
//  }


}
}
